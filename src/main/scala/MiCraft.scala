import processing.core._

object MiCraft extends AppletWithCamera {

  var chunk = new ChunkDrawable(Tag(RegionFile(
    "/home/wrath/Programmation/PLNC/scala/minecraft/src/main/resources",0,5)))



  
  override def setup(){
    noCursor
    size(640, 360, PConstants.P3D)
  }

  override def draw(){
    background(9,125,208)

    camera
    DebugMiCraft.drawMark(this)
    noStroke
    
    // Set up some different colored lights
  //  pointLight(51, 102, 255, width/3f, height/2f, 100) 
    //pointLight(200, 40, 60,  width/1.5f, height/2f, -150)

    // Raise overall light in scene 
//    ambientLight(170, 170, 100) 

    chunk.draw(this)
  }

  override def keyPressed(): Unit = {
    super.keyPressed()
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
