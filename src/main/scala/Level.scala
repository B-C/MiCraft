class Level(path: String, var visibleBlocks: List[Int]){
  val LEVEL_SIZE = 10
  private var chunks:Array[Array[Option[ChunkDrawable]]] = Array.ofDim(LEVEL_SIZE,LEVEL_SIZE)

  for(i <- 0 until LEVEL_SIZE)//Us offset LEVEL_SIZE/2 in order to get -1,-1...
    for(j <- 0 until LEVEL_SIZE)
      chunks(i)(j)=None

  def updateVisibleBlocks(id: Int): Unit = {
    if(visibleBlocks.exists(_==id))
      visibleBlocks=visibleBlocks.filterNot(_==id)
    else
      visibleBlocks=id::visibleBlocks

    chunks foreach(_ foreach(_ foreach(_.visibleBlocks(visibleBlocks))))
  }

  def getChunk(x: Int,z: Int): Option[ChunkDrawable] = {
    if(x<0 || x>=LEVEL_SIZE || z<0 || z>=LEVEL_SIZE)
      None
    else
      chunks(x)(z)
  }

  def loadChunk(i: Int, j:Int): Unit =
    chunks(i)(j) = RegionFile(path,i,j) map ( is => new ChunkDrawable(Tag(is), visibleBlocks, this))

  def draw(parent: processing.core.PApplet) =
    chunks foreach(_ foreach( _ foreach(_.draw(parent))))
}
