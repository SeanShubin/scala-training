package com.seanshubin.scala.training.core

class ImportParserImpl extends ImportParser {
  private val word = """([\w-]+)"""
  private val spaces = """\s+"""
  private val withColorPattern = word + spaces + word + spaces + "with sku" + spaces + word
  private val withoutColorPattern = word + spaces + "with sku" + spaces + word
  private val ItemWithColorRegex = withColorPattern.r
  private val ItemWithoutColorRegex = withoutColorPattern.r

  def parse(line: String): Item = {
    line match {
      case ItemWithColorRegex(color, name, sku) => Item(Some(color), name, sku)
      case ItemWithoutColorRegex(name, sku) => Item(None, name, sku)
      case _ => throw new RuntimeException(s"Unable to parse the text '$line' into a valid item")
    }
  }
}
