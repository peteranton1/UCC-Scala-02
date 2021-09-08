name := "UCC-Scala-02"

version := "0.1"

scalaVersion := "2.13.6"

val scalatestVersion = "3.2.9"
val akkaVersion = "2.6.16"
val catsVersion = "2.6.1"

libraryDependencies ++= Seq(

  // testing
  "org.scalatest" %% "scalatest" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",

  // Apache Commons
  "commons-io" % "commons-io" % "20030203.000550",

  // akka streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  // akka logging
  "ch.qos.logback" % "logback-classic" % "1.2.5",

  // cats
  "org.typelevel" %% "cats-core" % catsVersion,

)