package com.seanshubin.scala.training.core.file_system

trait FileSystem {
  def linesFromFileNamed(fileDirectory: String, fileName: String): Iterable[String]

  def writeLines(fileDirectory: String, fileName: String, lines: Iterable[String])
}
