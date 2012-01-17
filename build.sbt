import AssemblyKeys._

name := "simple-scheduler"

version := "0.1"

organization := "net.morrdusk"

scalaVersion := "2.9.1"

resolvers += "repo.novus rels" at "http://repo.novus.com/releases/"

resolvers += "repo.novus snaps" at "http://repo.novus.com/snapshots/"

resolvers += "repo.codahale.com" at "http://repo.codahale.com/"

resolvers += "Guice Maven" at "http://guice-maven.googlecode.com/svn/trunk"

// add compile dependencies
libraryDependencies ++= Seq(
    "net.databinder" %% "dispatch-oauth" % "0.7.8",
    "org.slf4j" % "slf4j-api" % "1.6.1",
    "ch.qos.logback" % "logback-classic" % "1.0.0",
    "org.scalatra" %% "scalatra" % "2.0.1",
    "org.scalatra" %% "scalatra-scalate" % "2.0.1",
    "org.eclipse.jetty" % "jetty-servlet" % "8.1.0.RC4",
    "com.novus" %% "salat-core" % "0.0.8-SNAPSHOT",
    "com.mongodb.casbah" % "casbah_2.9.0-1" % "2.1.5.0",
    "com.codahale" %% "jerkson" % "0.5.0",
    "org.quartz-scheduler" % "quartz" % "2.1.1",
    "joda-time" % "joda-time" % "2.0",
    "org.openid4java" % "openid4java-consumer" % "0.9.6"
)

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"

// begin: for the sbt-assembly plugin https://github.com/sbt/sbt-assembly
seq(assemblySettings: _*)

jarName in assembly := "simple-scheduler.jar"
// end: for the sbt-assembly plugin
