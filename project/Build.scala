

object TrainingBuild extends Build {
  lazy val parent = Project(id = "scala-training", base = file(".")) aggregate(web, core)
  lazy val core = Project(id = "scala-training-core", base = file("core"))
  lazy val web = Project(id = "scala-training-web", base = file("web")) dependsOn (core)
}
