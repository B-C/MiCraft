import processing.core._

object ProcessingTest extends AppletWithCamera {

  var ang = 0f
  var rows = 21
  var cols = 21
  var cubeCount = rows*cols
  var colSpan = 0
  var rowSpan = 0
  var rotspd = 2.0f
  var cubes = new Array[Cube](cubeCount)
  var angs = new Array[Float](cubeCount)
  var rotvals = new Array[Float](cubeCount)
  
  override def setup(){
    noCursor
    size(640, 360, PConstants.P3D)

    colSpan = width/(cols-1)
    rowSpan = height/(rows-1)

    // instantiate cubes
    for (i <- 0 until cubeCount){
      cubes(i) = new Cube(12, 12, 12, 0, 0, 0,this)
      rotspd += .01f
      rotvals(i) = rotspd
    }   
  }

  override def draw(){
    var cubeCounter = 0
    background(0)
    fill(200)

    camera
    DebugMiCraft.drawMark(this)
    noStroke
    
    // Set up some different colored lights
    pointLight(51, 102, 255, width/3f, height/2f, 100) 
    pointLight(200, 40, 60,  width/1.5f, height/2f, -150)

    // Raise overall light in scene 
    ambientLight(170, 170, 100) 

    // Translate, rotate and draw cubes
    for (i <- 0 until cols){
      for (j <- 0 until rows){
    	pushMatrix()
    	/* Translate each block.
         pushmatix and popmatrix add each cube
         translation to matrix, but restore
         original, so each cube rotates around its
         owns center */
    	translate(i * colSpan, j * rowSpan,-100)
    	//rotate each cube around y and x axes
    	rotateY(PApplet.radians(angs(cubeCounter)))
    	rotateX(PApplet.radians(angs(cubeCounter)))
    	cubes(cubeCounter).drawCube()
    	popMatrix()
    	cubeCounter+=1
      }
    }
    // Angs used in rotate function calls above
    for (i <- 0 until cubeCount){
      angs(i) += rotvals(i)/2
    }
  }

  override def keyPressed(): Unit = {
    super.keyPressed()
  }

  def main(args: Array[String]): Unit = {
    var frame = new javax.swing.JFrame("MiCraft")
    var applet = ProcessingTest
    frame.getContentPane().add(applet)
    applet.init
    frame.pack
    frame.setVisible(true)
  }
}
