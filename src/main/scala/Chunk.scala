/* ****************************************************************************
 * Class Chunk:
 *  In Minecraft a chunk is a 16*16*128 blocks Unit.
 *  Chunks are stored in tags.
 *  The class extracts the blocks from the tags and its position.
 *
 * Class ChunkDrawable:
 *  Extends Chunk.
 *  Analyzes the blocks and find those worthy of being displayed. That is to
 *  say ores and block in contact with air.
 *  Ore block might be displayed or not, depending on the value of
 *  visibleBlocks.
 *  Has a draw method.
 *
 *************************************************************************** */

class Chunk(tag: Tag){
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

class ChunkDrawable(tag: Tag, private var visibleBlocks: List[Int], level: Level) extends Chunk(tag) {
  private var drawable:List[Block] = List()
  private var structure: List[Block] = List()

  private var interrestingBlocks:Map[Int,List[Block]] = Map()

  private val xOffset = xPos*16
  private val zOffset = zPos*16

  init

  //Extracts blocks that have to be displayed
  def init = {
    var coal    : List[Block] = List()//16
    var iron    : List[Block] = List()//15
    var gold    : List[Block] = List()//14
    var diamond : List[Block] = List()//56
    var lapis   : List[Block] = List()//21
    var redstone: List[Block] = List()//73
    var obsidian: List[Block] = List()//49
    var clay    : List[Block] = List()//82

    var structure: List[Block] = List()

    for(x <- 0 until 16)
      for(z <- 0 until 16)
	for(y <- 0 until 128){
	  var id = getBlock(x,y,z)
	  Block(-(xOffset+x)*Block.SIZE, (zOffset+z)*Block.SIZE, -y*Block.SIZE, id) foreach (block =>
	    if(isDrawable(x,y,z))//Block in contact with air
	      structure=block::structure
	    else
	      id match{//check whether it is an interresting block
		case 16 => coal    =block::coal
		case 15 => iron    =block::iron
		case 14 => gold    =block::gold
		case 56 => diamond =block::diamond
		case 21 => lapis   =block::lapis
		case 73 => redstone=block::redstone
		case 49 => obsidian=block::obsidian
		case 82 => clay    =block::clay
		case _ => ()
	      })
	}

    this.structure=structure
    interrestingBlocks=Map(16 -> coal,
			   15 -> iron,
			   14 -> gold,
			   56 -> diamond,
			   21 -> lapis,
			   73 -> redstone,
			   49 -> obsidian,
			   82 -> clay)

    updateVisibleBlocks
    println("Chunk " + xPos +", " + zPos + " initialized")
  }

  //Set blocks to be drawn
  private def updateVisibleBlocks():Unit = {
    drawable=structure
    visibleBlocks foreach(id => drawable=interrestingBlocks(id):::drawable)
  }

  def updateVisibleBlocks(l: List[Int]): Unit = {
    visibleBlocks = l
    updateVisibleBlocks
  }

  private def isDrawable(x: Int, y:Int, z:Int): Boolean = {
    if(getBlock(x,y,z)==0)
      return false

    if(List(7,8,9,10,11).exists(_==getBlock(x,y,z)))//bedrock lava water visible
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
