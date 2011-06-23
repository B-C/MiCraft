import processing.core._

abstract class AppletWithCamera(cam:Camera) extends PApplet{
  private var lastMouseX = mouseX
  private var lastMouseY = mouseY
  // do multikey: http://wiki.processing.org/w/Multiple_key_presses

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

class Camera(var position: PVector,
	     private var phi:Float, private var theta:Float,
	     var verticalMotion:Boolean){

  private var target = new PVector(0,0,0)
  private var forward3D = new PVector(0,0,0)
  private var forward2D = new PVector(0,0,0)
  private var left = new PVector(0,0,0)

  private val up = new PVector(0,0,1)

  val mouseSensivity = 0.1f
  val lookKeySensivity = 4f
  val speed = 20

  vectorsFromAngles

  println("Camera initialized at "
	  +position.x+", "
	  +position.y+", "
	  +position.z)

  /******************************************************/

  private def vectorsFromAngles(): Unit ={
    if (phi > 89)
      phi = 89
    else if (phi < -89)
      phi = -89

    forward2D.x=PApplet.cos(PApplet.radians(theta))
    forward2D.y=PApplet.sin(PApplet.radians(theta))

    // spherical coordinate to cartesian
    var r = PApplet.cos(PApplet.radians(phi))
    forward3D.z = PApplet.sin(PApplet.radians(phi))
    forward3D.x = r*forward2D.x
    forward3D.y = r*forward2D.y

    left = up.cross(forward3D)
    left.normalize

    // compute where the camera look
    target = PVector.add(position, forward3D)
  }

  private def walk(distance: Float):Unit = {
    var v = if (!verticalMotion) PVector.mult(forward2D, distance)
	    else PVector.mult(forward3D, distance)

    position.add(v)
    target = PVector.add(position, forward3D)
  }

  private def strafe(distance: Float): Unit = {
    position.add(PVector.mult(left,distance))
    target = PVector.add(position, forward3D)
  }

  private def moveZ(distance:Float): Unit = {
    position.add(PVector.mult(up, -distance))
    target = PVector.add(position, forward3D)
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
    rotation(x*mouseSensivity, -y*mouseSensivity)

  def apply(parent: PApplet): Unit =
    parent.camera(position.x, position.y, position.z,
		  target.x, target.y, target.z, 0, 0, 1)
}
