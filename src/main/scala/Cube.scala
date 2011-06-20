class Cube(w:Int, h:Int, d:Int, shiftX:Int, shiftY:Int, shiftZ:Int, parent:processing.core.PApplet ){
  import processing.core._

  private def drawHelper(x:Float, y:Float, z:Float): Unit = {
    parent.vertex(x + shiftX, y + shiftY, z + shiftZ) 
  }

  def drawCube(): Unit = {

    parent.beginShape(PConstants.QUADS)

    // Front face
//   fill(0, 1, 1) //color
    drawHelper(-w/2, -h/2, -d/2) 
//   fill(0, 1, 1)
    drawHelper(w, -h/2, -d/2) 
    drawHelper(w, h, -d/2) 
    drawHelper(-w/2, h, -d/2) 


    // Back face
    drawHelper(-w/2, -h/2, d) 
    drawHelper(w, -h/2, d) 
    drawHelper(w, h, d) 
    drawHelper(-w/2, h, d)

    // Left face
    drawHelper(-w/2, -h/2, -d/2) 
    drawHelper(-w/2, -h/2, d) 
    drawHelper(-w/2, h, d) 
    drawHelper(-w/2, h, -d/2) 

    // Right face
    drawHelper(w, -h/2, -d/2) 
    drawHelper(w, -h/2, d) 
    drawHelper(w, h, d) 
    drawHelper(w, h, -d/2) 

    // Top face
    drawHelper(-w/2, -h/2, -d/2) 
    drawHelper(w, -h/2, -d/2) 
    drawHelper(w, -h/2, d) 
    drawHelper(-w/2, -h/2, d) 

    // Bottom face
    drawHelper(-w/2, h, -d/2) 
    drawHelper(w, h, -d/2) 
    drawHelper(w, h, d) 
    drawHelper(-w/2, h, d)

    parent.endShape() 
  }
}
