package com.seanshubin.scala.training.sample.data

import com.seanshubin.scala.training.core.file_system.{FileSystem, FileSystemImpl}

import scala.util.Random

trait ItemCatalogSampleDataGeneratorWiring {
  def itemCount: Int

  def itemDataFileDirectory: String

  def itemDataFileName: String

  lazy val charsetName: String = "UTF-8"
  lazy val items: Seq[String] = Seq(
    "shirt",
    "shoes",
    "socks",
    "belt",
    "pants",
    "shorts",
    "hat",
    "scarf")
  lazy val colors: Seq[Option[String]] = Seq(
    Some("red"),
    Some("yellow"),
    Some("green"),
    Some("blue"),
    Some("gray"),
    Some("black"),
    Some("white"),
    Some("brown"),
    None)
  lazy val seed: Int = 1
  lazy val random: Random = new Random(seed)
  lazy val randomIfc: RandomIfc = new RandomImpl(random)
  lazy val randomChooser: RandomChooser = new RandomChooserImpl(randomIfc)
  lazy val itemGenerator: ItemGenerator = new ItemGeneratorImpl(items, colors, randomChooser)
  lazy val itemFormatter: ItemFormatter = new ItemFormatterImpl
  lazy val itemLineGenerator: ItemLineGenerator = new ItemLineGeneratorImpl(itemGenerator, itemCount, itemFormatter)
  lazy val fileSystem: FileSystem = new FileSystemImpl(charsetName)
  lazy val sampleFileGenerator: SampleFileGenerator = new SampleFileGeneratorImpl(itemLineGenerator, fileSystem, itemDataFileDirectory, itemDataFileName)
}
