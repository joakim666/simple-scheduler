import AssemblyKeys._

name := "simple-scheduler"

version := "0.1"

organization := "net.morrdusk"

scalaVersion := "2.9.1"

// add compile dependencies
libraryDependencies ++= Seq(
    "net.databinder" %% "dispatch-oauth" % "0.7.8",
    "org.slf4j" % "slf4j-api" % "1.6.1",
    "org.slf4j" % "slf4j-simple" % "1.6.1"
)

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"

// begin: for the sbt-assembly plugin https://github.com/sbt/sbt-assembly
seq(assemblySettings: _*)

jarName in assembly := "simple-scheduler.jar"
// end: for the sbt-assembly plugin
