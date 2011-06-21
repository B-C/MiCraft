import processing.core._

class Camera(var position: PVector, var target: PVector, 
	     var verticalMotion:Boolean, parent:PApplet){
  
  var forward = new PVector(0,0,0)
  var left = new PVector(0,0,0)
  var theta = 0f
  var phi = 0f

  var lastMouseX = parent.mouseX
  var lastMouseY = parent.mouseY

  val up = new PVector(0,0,1)

  val mouseSensivity:Float = 0.1f
  val lookKeySensivity:Float = 1f
  
  vectorsFromAngles

  /******************************************************/

  private def vectorsFromAngles(): Unit ={
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

  private def rotation(dTheta:Float, dPhi:Float): Unit = {
    theta -= dTheta
    phi -= dPhi
    vectorsFromAngles
  }

  /******************************************************/

  def walkForward(distance:Float):Unit = {
    var v = PVector.mult(forward, distance)
    if (!verticalMotion) v.z=0
    position.add(v)
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



  def lookUp = rotation(0,lookKeySensivity)
  def lookDown = rotation(0,-lookKeySensivity)
  def lookLeft = rotation(-lookKeySensivity,0)
  def lookRight = rotation(lookKeySensivity,0)

  def mouseMotion(x:Int, y:Int): Unit = {
    rotation((x - lastMouseX) *mouseSensivity,(y - lastMouseY) *mouseSensivity)

    lastMouseX = x
    lastMouseY = y
  }

  def apply(): Unit =
    parent.camera(position.x, position.y, position.z, 
		  target.x, target.y, target.z, 
		  0, 0, 1)
}
