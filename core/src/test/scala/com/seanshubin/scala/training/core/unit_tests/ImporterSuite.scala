package com.seanshubin.scala.training.core.unit_tests

import com.seanshubin.scala.training.core._
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ImporterSuite extends FunSuite with EasyMockSugar {
  test("load lines") {
    val importParser = mock[ImportParser]
    val data = mock[DataStorage]
    val importer: Importer = new ImporterImpl(importParser, data)

    val fakeLine1 = "red bike sku-123"
    val fakeLine2 = "blue motorcycle sku-456"
    val fakeLine3 = "green skateboard sku-789"
    val fakeLines = Seq(fakeLine1, fakeLine2, fakeLine3)

    val fakeItem1 = Item("red", "bike", "sku-123")
    val fakeItem2 = Item("blue", "motorcycle", "sku-456")
    val fakeItem3 = Item("green", "skateboard", "sku-789")
    val fakeItems = Seq(fakeItem1, fakeItem2, fakeItem3)

    expecting {
      importParser.parse(fakeLine1).andReturn(fakeItem1)
      importParser.parse(fakeLine2).andReturn(fakeItem2)
      importParser.parse(fakeLine3).andReturn(fakeItem3)
      data.notThreadSafeAddItems(fakeItems)
    }

    whenExecuting(data, importParser) {
      importer.loadLines(fakeLines)
    }
  }
}
