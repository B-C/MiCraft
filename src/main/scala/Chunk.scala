
case class Chunk(tag: Tag){
  val data = tag.asInstanceOf[CompoundTag]
  val levelTag = data.value.head.asInstanceOf[CompoundTag]
  val x = levelTag.get("xPos").get.asInstanceOf[IntTag].value
  val z = levelTag.get("zPos").get.asInstanceOf[IntTag].value
  val entities = levelTag.get("Entities").get.asInstanceOf[ListTag]
  val blockData = levelTag.get("Blocks").get.asInstanceOf[ByteArrayTag]
  val mapData = levelTag.get("Data").get.asInstanceOf[ByteArrayTag]

  def getBlock(x: Int, y: Int, z: Int): Byte = 
    blockData.value(y + (z * 128) + (x * 128 * 16))
  
  

}

		for(int x=0;x<16;x++) {
			int xOff = (x * 128 * 16);
			for(int z=0;z<16;z++) {
				int zOff = (z * 128);
				int blockOffset = zOff + xOff-1;
				for(int y=0;y<128;y++) {
					blockOffset++;
					byte t = blockData.value[blockOffset];
					
					if(t < 1) {
						continue;
					}
