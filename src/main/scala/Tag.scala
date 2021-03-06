/* ****************************************************************************
 * Class Tag:
 *  Each Tag class store a value of a certain type and, if necessary, the name
 *  of the tag.
 *
 * Object Tag:
 *  Parses a DataInputStream and return the Tag it contains
 *
 *************************************************************************** */

sealed abstract class Tag(name: String, value: Any)

// TYPE: 0  NAME: TAG_End
// Payload: None.
// Note:    This tag is used to mark the end of a list. Cannot be named
case class EndTag() extends Tag("", None)

// TYPE: 1  NAME: TAG_Byte
// Payload: A single signed byte (8 bits)
case class ByteTag(name: String, value:Byte) extends Tag(name, value)

// TYPE: 2  NAME: TAG_Short
// Payload: A signed short (16 bits, big endian)
case class ShortTag(name: String, value:Short) extends Tag(name, value)

// TYPE: 3  NAME: TAG_Int
// Payload: A signed short (32 bits, big endian)
case class IntTag(name: String, value:Int) extends Tag(name, value)

// TYPE: 4  NAME: TAG_Long
// Payload: A signed long (64 bits, big endian)
case class LongTag(name: String, value:Long) extends Tag(name, value)

// TYPE: 5  NAME: TAG_Float
// Payload: A floating point value (32 bits, big endian, IEEE 754-2008, binary32)
case class FloatTag(name: String, value:Float) extends Tag(name, value)

// TYPE: 6  NAME: TAG_Double
// Payload: A floating point value (64 bits, big endian, IEEE 754-2008, binary64)
case class DoubleTag(name: String, value:Double) extends Tag(name, value)

// TYPE: 7  NAME: TAG_Byte_Array
// Payload: TAG_Int length
//          An array of bytes of unspecified format. The length of this array is <length> bytes
case class ByteArrayTag(name: String, value:Array[Byte]) extends Tag(name, value){
  override def toString = "ByteArrayTag(" + name + "," + value.length + " bytes)"
}

// TYPE: 8  NAME: TAG_String
// Payload: TAG_Short length
//          An array of bytes defining a string in UTF-8 format. The length of this array is <length> bytes
case class StringTag(name: String, value: String) extends Tag(name, value)

// TYPE: 9  NAME: TAG_List
// Payload: TAG_Byte tagId
//          TAG_Int length
//          A sequential list of Tags (not Named Tags), of type <typeId>. The length of this array is <length> Tags
// Notes:   All tags share the same type.
case class ListTag(name: String, value: List[Tag]) extends Tag(name, value){
  override def toString =
    "ListTag(" + name + ")" + value.map(_.toString.replaceAll("\n","\n\t")).mkString("{\n\t", "\n\t", "\n}")
}

// TYPE: 10 NAME: TAG_Compound
// Payload: A sequential list of Named Tags. This array keeps going until a TAG_End is found.
case class CompoundTag(name: String, value: List[Tag]) extends Tag(name, value){
  override def toString =
    "CompoundTag(" + name + ")" + value.map(_.toString.replaceAll("\n","\n\t")).mkString("{\n\t", "\n\t", "\n}")
  def get(key:String) = value.find( _ match {
    case _ :EndTag => false
    case t :Tag { def name():String } => t.name == key
    case _ => false
  })
}

object Tag {
  import java.io.DataInputStream

  private def readPayload( tagType: Byte, name: String, stream: DataInputStream): Tag =
    tagType match {
      case 0 => EndTag()
      case 1 => ByteTag(name, stream.readByte())
      case 2 => ShortTag(name, stream.readShort())
      case 3 => IntTag(name, stream.readInt())
      case 4 => LongTag(name, stream.readLong())
      case 5 => FloatTag(name, stream.readFloat())
      case 6 => DoubleTag(name, stream.readDouble())
      case 7 => {
	var len = stream.readInt()
	var data = Array.ofDim[Byte](len)
	stream.readFully(data)
	ByteArrayTag(name, data)
      }
      case 8 => new StringTag(name, stream.readUTF())
      case 9 => {
	var childType = stream.readByte()
	var listLength 	= stream.readInt()
	var list = List[Tag]()
	for(i <- 1 to listLength) {
	  var t:Tag = readPayload(childType, "", stream)
	  list = t :: list
	}
	ListTag(name, list reverse)//dirty
      }
      case 10 => CompoundTag(name, compound(stream))
    }

  private def compound(stream: DataInputStream): List[Tag] = {
    var tag = apply(stream)
    tag match {
      case e:EndTag =>  List[Tag]() ;
      case t:Tag => t :: compound(stream)
    }
  }

  def apply(stream: DataInputStream ): Tag = {
    var tagType = stream.readByte()
    if( tagType != 0)
      readPayload(tagType, stream.readUTF, stream)
    else
      EndTag()
  }
}
