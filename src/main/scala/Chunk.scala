
case class Chunk(tag: Tag){
  val data = tag.asInstanceOf[CompoundTag]
  val levelTag = data.value.head.asInstanceOf[CompoundTag]
  val x = levelTag.get("xPos").get.asInstanceOf[IntTag].value
  val z = levelTag.get("zPos").get.asInstanceOf[IntTag].value
  // val entities = levelTag.get("Entities").get.asInstanceOf[ListTag]
  val blockData = levelTag.get("Blocks").get.asInstanceOf[ByteArrayTag]
  // val mapData = levelTag.get("Data").get.asInstanceOf[ByteArrayTag]

  def getBlock(x: Int, y: Int, z: Int): Byte = 
    blockData.value(y + (z * 128) + (x * 128 * 16))
}

class ChunkDrawable(tag: Tag) extends Chunk(tag) {
  
  def draw(parent: processing.core.PApplet) = {
    for(x <- 0 until 16)
      for(z <- 0 until 16)
	for(y <- 0 until 128){
	  var id = getBlock(x,y,z)
	  if (id>=1){
	    parent.pushMatrix
	    parent.translate(x * Block.SIZE, z * Block.SIZE, -y * Block.SIZE)
	    Block(id).draw(parent)
	    parent.popMatrix
	  }
	}
  }
}
