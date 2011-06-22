case class Chunk(tag: Tag){
  val data = tag.asInstanceOf[CompoundTag]
  val levelTag = data.value.head.asInstanceOf[CompoundTag]
  val x = levelTag.get("xPos").get.asInstanceOf[IntTag].value
  val z = levelTag.get("zPos").get.asInstanceOf[IntTag].value
  // val entities = levelTag.get("Entities").get.asInstanceOf[ListTag]
  val blockData = levelTag.get("Blocks").get.asInstanceOf[ByteArrayTag]
  // val mapData = levelTag.get("Data").get.asInstanceOf[ByteArrayTag]

  def getBlock(x: Int, y: Int, z: Int): Byte =
    blockData.value(y + z*128 + x*128*16)
}

class ChunkDrawable(tag: Tag) extends Chunk(tag) {

  private val nbBlocks = 16*128*16

  val drawable = Array.ofDim[Boolean](nbBlocks)

  for(x <- 0 until 16)
    for(z <- 0 until 16)
      for(y <- 0 until 128)
	drawable(y + z*128 + x*128 *16)=isDrawable(x,y,z)

  private def isDrawable(x: Int, y:Int, z:Int): Boolean = {
    if( y==127 || y==0 || x==15 || x==0 || z==15 || z==0)
      true
    else
      getBlock(x+1,y,z)==0 ||
      getBlock(x-1,y,z)==0 ||
      getBlock(x,y+1,z)==0 ||
      getBlock(x,y-1,z)==0 ||
      getBlock(x,y,z+1)==0 ||
      getBlock(x,y,z+1)==0
  }

  def draw(parent: processing.core.PApplet) = {
    for(x <- 0 until 16){
      var offsetX = x*128*16
      for(z <- 0 until 16){
	var counter = offsetX + z*128
	for(y <- 0 until 128){
	  var id = blockData.value(counter)
	  if (id>=1 && drawable(counter)){
	    parent.pushMatrix
	    parent.translate(x * Block.SIZE, z * Block.SIZE, -y * Block.SIZE)
	    parent.scale(Block.SIZE/2)
	    Block.draw(id,parent)
	    parent.popMatrix
	  }
	  counter+=1
	}
      }
    }
  }
}
