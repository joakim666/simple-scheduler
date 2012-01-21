import sbt._
import Keys._
import sbtrelease.Release._

object SimpleSchedulerBuild extends Build {

  lazy val project = Project (
    "simple-scheduler",
    file("."),
    settings = Defaults.defaultSettings ++ releaseSettings ++ Seq(publish <<= publishTask)
  )

  // as I can't figure out how to change the release process to call assembly
  // instead of publish. Override publish and make it depend on assembly as a workaround
  val publish = TaskKey[Unit]("publish", "Override publish to depend on assembly")

  def publishTask = (fooTask) map { (out) =>
    println("Overridden publish called.")
  }

  val fooTask = TaskKey[String]("foo-task")
}
