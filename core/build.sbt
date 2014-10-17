name := "scala-training-core"

version := "1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  "org.easymock" % "easymock" % "3.1" % "test",
  "junit" % "junit" % "4.11" % "test"
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-explaintypes")
