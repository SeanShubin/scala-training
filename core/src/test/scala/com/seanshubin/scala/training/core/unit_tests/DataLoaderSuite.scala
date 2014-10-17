package com.seanshubin.scala.training.core.unit_tests

import com.seanshubin.scala.training.core.file_system.FileSystem
import com.seanshubin.scala.training.core.{DataLoader, DataLoaderImpl, Importer}
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class DataLoaderSuite extends FunSuite with EasyMockSugar {
  test("load lines") {
    val fileSystem = mock[FileSystem]
    val importer = mock[Importer]
    val itemDataFileDirectory = "data directory"
    val itemDataFileName = "file name"
    val dataLoader: DataLoader = new DataLoaderImpl(fileSystem, importer, itemDataFileDirectory, itemDataFileName)

    val lines = Seq("Line 1", "Line 2", "Line 3")

    expecting {
      fileSystem.linesFromFileNamed(itemDataFileDirectory, itemDataFileName).andReturn(lines)
      importer.loadLines(lines)
    }

    whenExecuting(fileSystem, importer) {
      dataLoader.load()
    }
  }
}
