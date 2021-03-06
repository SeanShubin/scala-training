name := "scala-training-web"

version := "1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.eclipse.jetty.aggregate" % "jetty-all" % "8.1.5.v20120716",
  "javax.servlet" % "javax.servlet-api" % "3.0.1",
  "org.apache.httpcomponents" % "httpcore" % "4.2.1",
  "org.apache.httpcomponents" % "httpclient" % "4.2.1",
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  "org.easymock" % "easymock" % "3.1" % "test",
  "junit" % "junit" % "4.11" % "test"
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-explaintypes")
