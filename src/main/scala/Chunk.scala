

case class Chunk(tag: Tag){
  val data = tag.asInstanceOf[CompoundTag]
  val levelTag = data.value.head.asInstanceOf[CompoundTag]
  val x = levelTag.get("xPos").get.asInstanceOf[IntTag].value
  val z = levelTag.get("zPos").get.asInstanceOf[IntTag].value
  val entities = levelTag.get("Entities").get.asInstanceOf[ListTag]
  val blockData = levelTag.get("Blocks").get.asInstanceOf[ByteArrayTag]
  val mapData = levelTag.get("Data").get.asInstanceOf[ByteArrayTag]
}
