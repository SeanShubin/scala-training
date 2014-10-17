package com.seanshubin.scala.training.web

import java.io.{FileInputStream, InputStreamReader}
import java.util.Properties

object ItemCatalogWebApplication extends App {
  val propertiesFileName = args(0)
  val properties = new Properties()
  properties.load(new InputStreamReader(new FileInputStream(propertiesFileName), "UTF-8"))

  val wired = new ItemCatalogWebWiring {
    lazy val itemDataFileDirectory: String = properties.get("item.data.file.directory").toString
    lazy val itemDataFileName: String = properties.get("item.data.file.name").toString
    lazy val port: Int = properties.get("port").toString.toInt
    lazy val host: String = properties.get("host").toString
  }

  wired.dataLoader.load()
  wired.httpServer.start()
  wired.smokeTest.runSmokeTests()
  wired.httpServer.join()
}
