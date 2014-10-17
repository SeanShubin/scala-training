package com.seanshubin.scala.training.sample.data

import java.io.{FileInputStream, InputStreamReader}
import java.util.Properties

object SampleDataGenerator extends App {
  val propertiesFileName = args(0)
  val properties = new Properties()
  properties.load(new InputStreamReader(new FileInputStream(propertiesFileName), "UTF-8"))

  val wiring = new ItemCatalogSampleDataGeneratorWiring {
    lazy val itemCount: Int = properties.get("item.count").toString.toInt
    lazy val itemDataFileDirectory: String = properties.get("item.data.file.directory").toString
    lazy val itemDataFileName: String = properties.get("item.data.file.name").toString
  }

  wiring.sampleFileGenerator.generate()
}
