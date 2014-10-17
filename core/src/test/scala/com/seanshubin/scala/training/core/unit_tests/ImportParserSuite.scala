package com.seanshubin.scala.training.core.unit_tests

import com.seanshubin.scala.training.core.{ImportParser, ImportParserImpl, Item}
import org.scalatest.FunSuite

class ImportParserSuite extends FunSuite {
  test("parse import data line") {
    val importString = "red car with sku a-123"
    val importParser: ImportParser = new ImportParserImpl()

    val actualItem = importParser.parse(importString)

    val expectedColor = "red"
    val expectedName = "car"
    val expectedSku = "a-123"
    val expectedItem = Item(expectedColor, expectedName, expectedSku)
    assert(expectedItem === actualItem)
  }

  test("sensible error message for invalid import data line") {
    val importString = "some nonsense"
    val importParser: ImportParser = new ImportParserImpl()

    val error = intercept[Throwable] {
      importParser.parse(importString)
    }

    val actualMessage = error.getMessage
    val expectedMessage = "Unable to parse the text 'some nonsense' into a valid item"
    assert(expectedMessage === actualMessage)
  }
}
