import processing.core._

class Block(d:Int, parent: PApplet){

  def draw = {
    parent.fill(204, 102, 0)
    parent.box(Block.SIZE)
  }
}

object Block{
  val SIZE = 20
  def apply(id:Int, parent: PApplet):Block = new Block(id, parent)
}
