import processing.core._

class MiCraft(path:String, levelData:CompoundTag) extends AppletWithCamera(MiCraft.initCamera(levelData)) {
  private var level = new Level(path)
  private var pause = false

  override def setup(){
    noCursor
    size(640, 360, PConstants.P3D)
    textureMode(PConstants.NORMAL)
    noStroke
  }

  override def draw(){
    background(9,125,208)
    camera
    level.draw(cam.position.x.toInt, cam.position.y.toInt,this)
  }

  override def keyPressed(): Unit = {
    import java.awt.event.KeyEvent
    super.keyPressed()
    if (key == PConstants.CODED)
      keyCode match{
    	case KeyEvent.VK_F1  => level.updateVisibleBlocks(16) //coal
    	case KeyEvent.VK_F2  => level.updateVisibleBlocks(15) //iron
    	case KeyEvent.VK_F3  => level.updateVisibleBlocks(14) //gold
    	case KeyEvent.VK_F4  => level.updateVisibleBlocks(56) //diamond
    	case KeyEvent.VK_F5  => level.updateVisibleBlocks(21) //lapis lazuli
    	case KeyEvent.VK_F6  => level.updateVisibleBlocks(73) //redstone
    	case KeyEvent.VK_F7  => level.updateVisibleBlocks(49) //obsidian
    	case KeyEvent.VK_F8  => level.updateVisibleBlocks(82) //clay
    	case _ => ()
      }
    else
      key.toUpper match{
	case 'P' => 
	  if(pause){pause = false; loop}
	  else{pause = true; noLoop}
	case 'C' => cam=MiCraft.initCamera(levelData)
	case 'R' => level.reset
	case _   => ()
      }
  }
}

object MiCraft{
  private def initCamera(levelData:CompoundTag) ={
    val player = levelData.get("Player").get.asInstanceOf[CompoundTag]
    //dim 0 real world, -1 nether
    val levelDimension = player.get("Dimension").get.asInstanceOf[IntTag].value

    if(levelDimension==0){
      val playerPos =
	player.get("Pos").get.asInstanceOf[ListTag].value.asInstanceOf[List[DoubleTag]]
      val playerRotation =
	player.get("Rotation").get.asInstanceOf[ListTag].value.asInstanceOf[List[FloatTag]]

      new Camera(new PVector(-playerPos(0).value.toFloat*Block.SIZE,
			     playerPos(2).value.toFloat*Block.SIZE,
			     -playerPos(1).value.toFloat*Block.SIZE),
		 playerRotation(1).value, playerRotation(0).value, false)
    }
    else
      new Camera(new PVector(70f, 35.0f, -120), 0, 0, false)
  }
}

object MiCraftLoader {
  import java.io.File

  val saves = new File(System.getProperty("user.home"), ".minecraft/saves")

  def readLevelDat(f:File):CompoundTag ={
    import java.io.DataInputStream
    import java.io.FileInputStream
    import java.util.zip.GZIPInputStream

    var stream = new DataInputStream(new GZIPInputStream(new FileInputStream(f)))
    Tag(stream).asInstanceOf[CompoundTag]
  }

  def chooseMap(): String = {
    val list = saves.list.filter(str => new File(saves, str).isDirectory)
    var i=0

    println("Choose a map:")

    list foreach (str => {println(i+": "+str); i+=1})
    print(i+": Other\n> ")

    i=readInt

    if(i>=list.size || i<0){
      print("Path to the map:\n> ")
      readLine
    }
    else
      saves.getAbsoluteFile+"/"+list(i)
  }



  def main(args: Array[String]): Unit = {
    val path = chooseMap

    val levelTag = readLevelDat(new File(path+"/level.dat"))
    val levelData = levelTag.get("Data").get.asInstanceOf[CompoundTag]
    val levelName = levelData.get("LevelName").get.asInstanceOf[StringTag].value

    var applet = new MiCraft(path, levelData)

    var frame = new javax.swing.JFrame("MiCraft - "+levelName)

    frame.getContentPane().add(applet)
    applet.init
    frame.pack
    frame.setVisible(true)
  }
}
