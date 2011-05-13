
import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with assembly.AssemblyBuilder{

  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at
                            "http://scala-tools.org/repo-snapshots"

  val scalatest = "org.scalatest" % "scalatest" % "1.3"

  val lift_json = "net.liftweb" %% "lift-json" % "2.3"

}
