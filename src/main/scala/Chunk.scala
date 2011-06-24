case class Chunk(tag: Tag){
  val data = tag.asInstanceOf[CompoundTag]
  val levelTag = data.value.head.asInstanceOf[CompoundTag]
  val xPos = levelTag.get("xPos").get.asInstanceOf[IntTag].value
  val zPos = levelTag.get("zPos").get.asInstanceOf[IntTag].value
  // val entities = levelTag.get("Entities").get.asInstanceOf[ListTag]
  val blockData = levelTag.get("Blocks").get.asInstanceOf[ByteArrayTag]
  // val mapData = levelTag.get("Data").get.asInstanceOf[ByteArrayTag]

  def getBlock(x: Int, y: Int, z: Int): Byte =
    blockData.value(y + z*128 + x*128*16)
}

class ChunkDrawable(tag: Tag, private var _visibleBlocks: List[Int], level: Level) extends Chunk(tag) {
  private var drawable:List[Block] = List()
  private val xOffset = xPos*16
  private val zOffset = zPos*16
  visibleBlocks(_visibleBlocks)

  def visibleBlocks(l: List[Int]) = {
    _visibleBlocks=l

    drawable=List()

    for(x <- 0 until 16)
      for(z <- 0 until 16)
	for(y <- 0 until 128)
	  if(isDrawable(x,y,z))
	    Block.textures.get(getBlock(x,y,z)) match{
	      case Some(tex) => drawable = new Block(-(xOffset+x)*Block.SIZE,
						     (zOffset+z)*Block.SIZE,
						     -y*Block.SIZE,tex)::drawable
	      case None => ()
	    }


  }

  private def isDrawable(x: Int, y:Int, z:Int): Boolean = {
    if(getBlock(x,y,z)==0)
      return false
    if(_visibleBlocks.exists(_==getBlock(x,y,z)))
      return true

    if(y == 0 || y == 127)
      return true
    if(Block.isTransparent(getBlock(x,y+1,z)) ||
       Block.isTransparent(getBlock(x,y-1,z)))
      return true

    var blocks:Array[Option[Int]] = Array(
      if(x==0)
	level.getChunk(xPos-1,zPos) map (_.getBlock(15,y,z))
      else
	Some(getBlock(x-1,y,z)),
      if(x==15)
	level.getChunk(xPos+1,zPos) map (_.getBlock(0,y,z))
      else
	Some(getBlock(x+1,y,z)),
      if(z==0)
	level.getChunk(xPos,zPos-1) map (_.getBlock(x,y,15))
      else
	Some(getBlock(x,y,z-1)),
      if(z==15)
	level.getChunk(xPos,zPos+1) map (_.getBlock(x,y,0))
      else
	Some(getBlock(x,y,z+1)))

    return !(blocks forall( b => !Block.isTransparent(b.getOrElse(1))))
  }

  def draw(parent: processing.core.PApplet) = {
    drawable foreach(_.draw(parent))
  }

}
