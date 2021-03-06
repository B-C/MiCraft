/* ****************************************************************************
 * Class Level:
 *  A minecraft level
 *  Load chunks (with RegionFile) thanks to actors
 *  Set the number of chunks drawn
 *  Update the visible blocks of chunks
 *  Has a draw method
 *
 *************************************************************************** */

class Level(path: String){
  val LEVEL_SIZE = 128
  private var nbChunksDraw = 1
  private var chunks:Array[Array[Option[ChunkDrawable]]] = Array.ofDim(LEVEL_SIZE,LEVEL_SIZE)
  private var chunksLoaded:Array[Array[Boolean]] = Array.ofDim(LEVEL_SIZE,LEVEL_SIZE)
  private var visibleBlocks: List[Int] = List()

  for(i <- 0 until LEVEL_SIZE)
    for(j <- 0 until LEVEL_SIZE)
      chunks(i)(j)=None

  def moreChunks = {
    nbChunksDraw+=1
    println(nbChunksDraw + " chunks by " + nbChunksDraw + " chunks drawn")
  }

  def lessChunks = {
    if(nbChunksDraw>1)
      nbChunksDraw-=1
    println(nbChunksDraw + " chunks by " + nbChunksDraw + " chunks drawn")
  }

  def updateVisibleBlocks(id: Int): Unit = {
    if(visibleBlocks.exists(_==id))
      visibleBlocks=visibleBlocks.filterNot(_==id)
    else
      visibleBlocks=id::visibleBlocks
    chunks foreach(_ foreach(_ foreach(_.updateVisibleBlocks(visibleBlocks))))
  }

  def isInLevel(x: Int,z: Int): Boolean =
    !(x < -LEVEL_SIZE/2 || x >= LEVEL_SIZE/2 ||
      z < -LEVEL_SIZE/2 || z >= LEVEL_SIZE/2)

  def getChunk(x: Int,z: Int): Option[ChunkDrawable] =
    if(isInLevel(x, z))
      chunks(x+LEVEL_SIZE/2)(z+LEVEL_SIZE/2)
    else
      None

  def isLoaded(i: Int, j: Int) = chunksLoaded(i+LEVEL_SIZE/2)(j+LEVEL_SIZE/2)

  def loadChunk(i: Int, j:Int): Unit = {
    val chunk = RegionFile(path,i,j) map (is =>
      new ChunkDrawable(Tag(is), visibleBlocks, this))
    chunks(i+LEVEL_SIZE/2)(j+LEVEL_SIZE/2) = chunk

    //re-inits chunks which have a new neighborg, to update visible blocks
    //at the border(overkill)
    chunk match{
      case Some(c) => {
	val update = (i: Int, j: Int) =>
	  if(isInLevel(i,j) && isLoaded(i,j))
	     getChunk(i,j) foreach(_.init)

	update(i-1,j)
	update(i+1,j)
	update(i,j-1)
	update(i,j+1)
      }
      case None => ()
    }
  }

  def draw(camX: Int, camZ: Int, parent: processing.core.PApplet) ={
    import scala.actors.Actor._

    val chunkX = -camX/Block.SIZE/16
    val chunkZ = camZ/Block.SIZE/16-1

    for(i <- chunkX-nbChunksDraw/2 until chunkX+(nbChunksDraw+1)/2)
      for(j <- chunkZ-nbChunksDraw/2 until chunkZ+(nbChunksDraw+1)/2)
	if(isInLevel(i,j)) {
	  if(!isLoaded(i,j)){
	    println("Request Chunk "+i+", "+j)
	    var a = actor {
	      react {
		case Tuple3(l: Level, i: Int, j: Int) => l.loadChunk(i,j)
	      }
	    }
	    a.start ! Tuple3(this, i, j)
	    chunksLoaded(i+LEVEL_SIZE/2)(j+LEVEL_SIZE/2)=true
	  }
	  getChunk(i,j) map (_.draw(parent))
	}
  }
}
