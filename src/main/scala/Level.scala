class Level(path: String, var visibleBlocks: List[Int]){
  val LEVEL_SIZE = 128
  val BLOCK_DRAW = 3
  private var chunks:Array[Array[Option[ChunkDrawable]]] = Array.ofDim(LEVEL_SIZE,LEVEL_SIZE)

  for(i <- 0 until LEVEL_SIZE)
    for(j <- 0 until LEVEL_SIZE)
      chunks(i)(j)=None

  def updateVisibleBlocks(id: Int): Unit = {
    if(visibleBlocks.exists(_==id))
      visibleBlocks=visibleBlocks.filterNot(_==id)
    else
      visibleBlocks=id::visibleBlocks

    chunks foreach(_ foreach(_ foreach(_.visibleBlocks(visibleBlocks))))
  }

  def isInLevel(x: Int,z: Int): Boolean =
    !(x<0 || x>=LEVEL_SIZE || z<0 || z>=LEVEL_SIZE)

  def getChunk(x: Int,z: Int): Option[ChunkDrawable] =
    if(isInLevel(x,z)) chunks(x)(z) else None

  def loadChunk(i: Int, j:Int): Unit =
    chunks(i)(j) = RegionFile(path,i,j) map ( is => new ChunkDrawable(Tag(is), visibleBlocks, this))

  def draw(camX: Int, camZ: Int, parent: processing.core.PApplet) ={
    val chunkX = camX/Block.SIZE/16
    val chunkZ = camZ/Block.SIZE/16

    for(i <- chunkX-BLOCK_DRAW until chunkX+BLOCK_DRAW)
      for(j <- chunkZ-BLOCK_DRAW until chunkZ+BLOCK_DRAW)
	if(isInLevel(i,j)) {
	  if(chunks(i)(j)==None){
	    println("Load Chunk "+i+", "+j)
	    loadChunk(i,j)
	  }

	  chunks(i)(j) map (_.draw(parent))
	}
  }
}

//Us offset LEVEL_SIZE/2 in order to get -1,-1...
