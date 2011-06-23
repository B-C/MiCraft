import processing.core._

class MiCraft(path:String, cam:Camera) extends AppletWithCamera(cam) {

  var level = new Level(path, List(7,8,9,10,11))//bedrock lava water visible

  override def setup(){
    noCursor
    size(640, 360, PConstants.P3D)
    textureMode(PConstants.NORMAL)
    noStroke
  }

  override def draw(){
    background(9,125,208)
    camera

    /***/
    DebugMiCraft.drawMark(this)
    /**/

    // Set up some different colored lights
    //pointLight(51, 102, 255, width/3f, height/2f, 100)
    //pointLight(200, 40, 60,  width/1.5f, height/2f, -150)

    // Raise overall light in scene
    //ambientLight(170, 170, 100)

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
    	case KeyEvent.VK_F7  => level.updateVisibleBlocks(86) //pumpkin
    	case KeyEvent.VK_F8  => level.updateVisibleBlocks(82) //clay
    	case KeyEvent.VK_F9  => level.updateVisibleBlocks(52) //spawner
    	case KeyEvent.VK_F10 => level.updateVisibleBlocks(49) //obsidian
    	case _ => ()
      }
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

  def initCamera(player:CompoundTag) ={
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

  def main(args: Array[String]): Unit = {
    val path = chooseMap

    val levelTag = readLevelDat(new File(path+"/level.dat"))
    val levelData = levelTag.get("Data").get.asInstanceOf[CompoundTag]
    val levelName = levelData.get("LevelName").get.asInstanceOf[StringTag].value
    val levelPlayer = levelData.get("Player").get.asInstanceOf[CompoundTag]

    var applet = new MiCraft(path, initCamera(levelPlayer))

    var frame = new javax.swing.JFrame("MiCraft")

    frame.getContentPane().add(applet)
    applet.init
    frame.pack
    frame.setVisible(true)
  }
}
