package com.seanshubin.scala.training.core

import com.seanshubin.scala.training.core.file_system.FileSystem

class DataLoaderImpl(fileSystem: FileSystem, importer: Importer, itemDataFileDirectory: String, itemDataFileName: String) extends DataLoader {
  def load() {
    val dataLines = fileSystem.linesFromFileNamed(itemDataFileDirectory, itemDataFileName)
    importer.loadLines(dataLines)
  }
}
