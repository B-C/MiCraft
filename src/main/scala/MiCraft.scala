import processing.core._

class MiCraft(path:String) extends AppletWithCamera {

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
  
  def main(args: Array[String]): Unit = {
    
    
    print("Path to map:\n> ")
    var path = readLine
//    var path = "src/main/resources"

    var frame = new javax.swing.JFrame("MiCraft")

    var applet = new MiCraft(path)
    frame.getContentPane().add(applet)
    applet.init
    frame.pack
    frame.setVisible(true)
  }
}
