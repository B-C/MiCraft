import processing.core._

class Camera(var position: PVector, var target: PVector, parent:PApplet){
  
  var forward = new PVector(0,0,0)
  var left = new PVector(0,0,0)
  var theta = 0f
  var phi = 0f

  var lastMouseX = parent.mouseX
  var lastMouseY = parent.mouseY

  val up = new PVector(0,0,1)

  val sensivity:Float = 0.1f

//var verticalMotionActive: Boolean  

  VectorsFromAngles

  def VectorsFromAngles(): Unit ={
    if (phi > 89)
        phi = 89
    else if (phi < -89)
        phi = -89;

    //coordonnées sphériques to cartésiennes
    var r_temp = PApplet.cos(PApplet.radians(phi))
    forward.z = PApplet.sin(PApplet.radians(phi))
    forward.x = r_temp*PApplet.cos(PApplet.radians(theta))
    forward.y = r_temp*PApplet.sin(PApplet.radians(theta))


    left = up.cross(forward)
    left.normalize

    //compute ce que regarde la caméra
    target = PVector.add(position, forward)
  }

  def walkForward(distance:Float):Unit = {
    position.add(PVector.mult(forward,distance))
    target = PVector.add(position, forward)
  }
  def walkBackwards(distance: Float): Unit = walkForward(-distance)
  def strafeLeft(distance: Float): Unit = {
    position.add(PVector.mult(left,distance))
    target = PVector.add(position, forward)
  }
  def strafeRight(distance: Float): Unit = strafeLeft(-distance)

  def moveUp(distance:Float): Unit = {
    position.add(PVector.mult(up, -distance))
    target = PVector.add(position, forward)
  }
  def moveDown(distance:Float): Unit = moveUp(-distance)

  def mouseMotion(x:Int, y:Int): Unit = {
    theta -= (x - lastMouseX) *sensivity
    phi -= (y - lastMouseY) *sensivity

    lastMouseX = x
    lastMouseY = y

    VectorsFromAngles
  }

  def apply(): Unit =
    parent.camera(position.x, position.y, position.z, target.x, target.y, target.z, 0, 0, 1)
}
