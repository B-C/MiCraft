object DebugMiCraft{
  val on = true

  def drawMark(parent:processing.core.PApplet):Unit = 
    if(on){
      parent.stroke(255,0,0)
      parent.line(0,0,0,1000,0,0)
      parent.stroke(0,255,0)
      parent.line(0,0,0,0,1000,0)
      parent.stroke(0,0,255)
      parent.line(0,0,0,0,0,1000)
    }
}
