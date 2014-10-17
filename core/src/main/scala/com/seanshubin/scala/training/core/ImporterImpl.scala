package com.seanshubin.scala.training.core

class ImporterImpl(importParser: ImportParser, data: DataStorage) extends Importer {
  def loadLines(lines: Iterable[String]) {
    val items = lines.map(importParser.parse)
    data.notThreadSafeAddItems(items)
  }
}
