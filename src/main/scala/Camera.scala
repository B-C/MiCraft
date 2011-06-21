import processing.core._

abstract class AppletWithCamera extends PApplet{
  private var lastMouseX = mouseX
  private var lastMouseY = mouseY
  // do multikey: http://wiki.processing.org/w/Multiple_key_presses

  private var cam = new Camera(new PVector(70f, 35.0f, 120), 
			       new PVector(width/2.0f, height/2.0f, 0), false)

  override def keyPressed(): Unit = {
    import java.awt.event.KeyEvent
    if (key == PConstants.CODED)
      keyCode match{
	case PConstants.RIGHT => cam.lookRight
	case PConstants.LEFT => cam.lookLeft
 	case PConstants.UP => cam.lookUp
	case PConstants.DOWN => cam.lookDown
	case KeyEvent.VK_PAGE_UP => cam.moveUp
	case KeyEvent.VK_PAGE_DOWN => cam.moveDown
	case _ => ()
      }
    else
      key.toUpper match{
	case 'A' => cam.verticalMotion = !cam.verticalMotion
	case 'Z' => cam.walkForward
	case 'S' => cam.walkBackward
	case 'Q' => cam.strafeLeft
	case 'D' => cam.strafeRight
	case _   => ()
      }
  }

  override def mouseMoved(): Unit = {
    var x = mouseX
    var y = mouseY
    cam.mouseMotion(x - lastMouseX, y - lastMouseY)
    lastMouseX = x
    lastMouseY = y
  }

  override def camera = cam(this)
}

class Camera(private var position: PVector, 
	     private var target: PVector, 
	     var verticalMotion:Boolean){

  private var forward = new PVector(0,0,0)
  private var left = new PVector(0,0,0)
  private var theta = 0f
  private var phi = 0f

  private val up = new PVector(0,0,1)

  val mouseSensivity = 0.1f
  val lookKeySensivity = 1f
  val speed = 20
  
  vectorsFromAngles

  /******************************************************/

  private def vectorsFromAngles(): Unit ={
    if (phi > 89)
      phi = 89
    else if (phi < -89)
      phi = -89

    // spherical coordinate to cartesian
    var r = PApplet.cos(PApplet.radians(phi))
    forward.z = PApplet.sin(PApplet.radians(phi))
    forward.x = r*PApplet.cos(PApplet.radians(theta))
    forward.y = r*PApplet.sin(PApplet.radians(theta))

    left = up.cross(forward)
    left.normalize

    // compute where the camera look
    target = PVector.add(position, forward)
  }

  private def walk(distance: Float):Unit = {
    var v = PVector.mult(forward, distance)
    if (!verticalMotion) v.z = 0
    position.add(v)
    target = PVector.add(position, forward)
  }

  private def strafe(distance: Float): Unit = {
    position.add(PVector.mult(left,distance))
    target = PVector.add(position, forward)
  }

  private def moveZ(distance:Float): Unit = {
    position.add(PVector.mult(up, -distance))
    target = PVector.add(position, forward)
  }

  private def rotation(dTheta:Float, dPhi:Float): Unit = {
    theta -= dTheta
    phi -= dPhi
    vectorsFromAngles
  }

  /******************************************************/
  
  def walkForward = walk(speed)
  def walkBackward = walk(-speed)

  def strafeLeft = strafe(speed)
  def strafeRight = strafe(-speed)

  def moveUp = moveZ(speed)
  def moveDown = moveZ(-speed)

  def lookUp = rotation(0,lookKeySensivity)
  def lookDown = rotation(0,-lookKeySensivity)
  def lookLeft = rotation(-lookKeySensivity,0)
  def lookRight = rotation(lookKeySensivity,0)

  def mouseMotion(x: Int, y: Int) =
    rotation(x*mouseSensivity, y*mouseSensivity)

  def apply(parent: PApplet): Unit =
    parent.camera(position.x, position.y, position.z, 
		  target.x, target.y, target.z, 0, 0, 1)
}
