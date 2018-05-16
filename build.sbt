name := "intel-a-star"

version := "0.1"

scalaVersion := "2.12.6"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalaz" %% "scalaz-core" % "7.2.22"
)