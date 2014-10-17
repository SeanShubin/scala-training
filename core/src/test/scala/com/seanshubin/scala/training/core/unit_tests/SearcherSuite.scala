package com.seanshubin.scala.training.core.unit_tests

import com.seanshubin.scala.training.core._
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class SearcherSuite extends FunSuite with EasyMockSugar {
  test("query for sku by color and name") {
    val queryParser = mock[QueryParser]
    val data = mock[DataStorage]
    val searcher: Searcher = new SearcherImpl(queryParser, data)

    val queryString = "red car"
    val queryColor = "red"
    val queryName = "car"
    val fakeQuery = ColorAndNameQuery(queryColor, queryName)
    val sku1 = "sku-1"
    val sku2 = "sku-2"
    val fakeItems = Seq(
      Item("red", "car", sku1),
      Item("red", "car", sku2))
    val expectedSkus = Seq(sku1, sku2)

    expecting {
      queryParser.parse(queryString).andReturn(fakeQuery)
      data.searchByColorAndName(queryColor, queryName).andReturn(fakeItems)
    }

    whenExecuting(data, queryParser) {
      val actualSkus = searcher.queryForSkus(queryString)
      assert(actualSkus === expectedSkus)
    }
  }

  test("query for sku by name") {
    val queryParser = mock[QueryParser]
    val data = mock[DataStorage]
    val searcher: Searcher = new SearcherImpl(queryParser, data)

    val queryString = "car"
    val queryName = "car"
    val fakeQuery = NameQuery(queryName)
    val sku1 = "sku-1"
    val sku2 = "sku-2"
    val fakeItems = Seq(
      Item("red", "car", sku1),
      Item("orange", "car", sku2))
    val expectedSkus = Seq(sku1, sku2)

    expecting {
      queryParser.parse(queryString).andReturn(fakeQuery)
      data.searchByName(queryName).andReturn(fakeItems)
    }

    whenExecuting(data, queryParser) {
      val actualSkus = searcher.queryForSkus(queryString)
      assert(actualSkus === expectedSkus)
    }
  }
}
