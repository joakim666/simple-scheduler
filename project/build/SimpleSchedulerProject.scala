import sbt._
import Process._

class SimpleSchedulerProject(info: ProjectInfo) extends DefaultProject(info) with ProguardProject {
  val dispatch = "net.databinder" %% "dispatch-oauth" % "0.7.8"
  val slf4j = "org.slf4j" % "slf4j-api" % "1.6.1"
  val slf4j_simple = "org.slf4j" % "slf4j-simple" % "1.6.1"

  override def proguardInJars = super.proguardInJars +++ scalaLibraryPath

  override def proguardOptions = List(
    proguardKeepLimitedSerializability,
    proguardKeepAllScala,
    proguardKeepMain("Main"),
    "-dontshrink",
    "-dontoptimize",
    "-dontobfuscate"
  )
}
