import processing.core._

object MiCraft extends AppletWithCamera {
  
  
  var visibleBlocks: List[Int] = List(8,910,11) //lava water visible
  var chunk = new ChunkDrawable(Tag(RegionFile("src/main/resources",0,5)), visibleBlocks)



  
  override def setup(){
    noCursor
    size(640, 360, PConstants.P3D)
    textureMode(PConstants.NORMALIZED)
   // noStroke
  }

  override def draw(){
    background(9,125,208)
    camera

    /***/
    DebugMiCraft.drawMark(this)
    noStroke
    /**/
   
    
    // Set up some different colored lights
    //pointLight(51, 102, 255, width/3f, height/2f, 100) 
    //pointLight(200, 40, 60,  width/1.5f, height/2f, -150)

    // Raise overall light in scene 
    //ambientLight(170, 170, 100) 

    chunk.draw(this)
  }

  def listUpdate(id:Int) = {
    if(visibleBlocks.exists(_==id))
      visibleBlocks=visibleBlocks.filterNot(_==id)
    else
      visibleBlocks=id::visibleBlocks

    chunk.visibleBlocks(visibleBlocks)
  }
       

  override def keyPressed(): Unit = {
    import java.awt.event.KeyEvent
    super.keyPressed()
    if (key == PConstants.CODED)
      keyCode match{
    	case KeyEvent.VK_F1 => listUpdate(16) //coal
    	case KeyEvent.VK_F2 => listUpdate(15) //iron
    	case KeyEvent.VK_F3 => listUpdate(14) //gold
    	case KeyEvent.VK_F4 => listUpdate(56) //diamond
    	case KeyEvent.VK_F5 => listUpdate(21) //lapis lazuli
    	case KeyEvent.VK_F6 => listUpdate(73) //redstone
    	case KeyEvent.VK_F7 => listUpdate(49) //obsidian
    	case KeyEvent.VK_F8 => listUpdate(82) //clay
    	case KeyEvent.VK_F9 => listUpdate(52) //spawner
    	case KeyEvent.VK_F10 => listUpdate(86) //pumpkin
    	case _ => ()
      }
  }

  def main(args: Array[String]): Unit = {
    var frame = new javax.swing.JFrame("MiCraft")
    var applet = MiCraft
    frame.getContentPane().add(applet)
    applet.init
    frame.pack
    frame.setVisible(true)
  }
}
