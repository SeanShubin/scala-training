package com.seanshubin.scala.training.sample.data

import com.seanshubin.scala.training.core.file_system.FileSystem

class SampleFileGeneratorImpl(itemLineGenerator: ItemLineGenerator, fileSystem: FileSystem, itemDataFileDirectory: String, itemDataFileName: String) extends SampleFileGenerator {
  def generate() {
    val generatedLines = itemLineGenerator.generateItemLines()
    fileSystem.writeLines(itemDataFileDirectory, itemDataFileName, generatedLines)
  }
}
