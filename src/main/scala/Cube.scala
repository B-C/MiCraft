import processing.core._

class Cube(w:Int, h:Int, d:Int, shiftX:Int, shiftY:Int, shiftZ:Int, parent:PApplet ){
  

  private def drawHelper(x:Float, y:Float, z:Float): Unit = {
    parent.vertex(x + shiftX, y + shiftY, z + shiftZ) 
  }

  def draw(): Unit = {
    parent.beginShape(PConstants.QUADS)
    
    // Front face
    drawHelper(-w/2, -h/2, -d/2) 
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

class Block(d:Int, parent: PApplet) extends Cube(Block.SIZE, Block.SIZE, Block.SIZE, 
						 0, 0, 0, parent) {

  override def draw = {
    parent.fill(204, 102, 0)
    super.draw
  }

}

object Block{
  val SIZE = 20
  def apply(id:Int, parent: PApplet):Block = new Block(id, parent)
}
