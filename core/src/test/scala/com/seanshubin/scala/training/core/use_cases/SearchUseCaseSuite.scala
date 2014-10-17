package com.seanshubin.scala.training.core.use_cases

import com.seanshubin.scala.training.core._
import org.scalatest.FunSuite

class SearchUseCaseSuite extends FunSuite {
  test("search by color and name") {
    val queryParser = new QueryParserImpl
    val dataStorage = new InMemoryDataStorage
    val searcher = new SearcherImpl(queryParser, dataStorage)
    val importParser = new ImportParserImpl
    val importer = new ImporterImpl(importParser, dataStorage)

    val importLines = Seq(
      "orange bike      with sku a-100",
      "red    lawnmower with sku a-101",
      "red    bike      with sku a-102",
      "green  lawnmower with sku a-103",
      "red    lawnmower with sku a-104"
    )
    val query = "red lawnmower"

    importer.loadLines(importLines)
    val actualSkus = searcher.queryForSkus(query)

    val expectedSkus = Seq("a-101", "a-104")
    assert(actualSkus === expectedSkus)
  }

  test("search by color and name, refactor configuration into trait") {
    val importLines = Seq(
      "orange bike      with sku a-100",
      "red    lawnmower with sku a-101",
      "red    bike      with sku a-102",
      "green  lawnmower with sku a-103",
      "red    lawnmower with sku a-104"
    )

    val query = "red lawnmower"

    val configuration = new ItemCatalogCoreWiring {}
    configuration.importer.loadLines(importLines)

    val actualSkus = configuration.searcher.queryForSkus(query)

    val expectedSkus = Seq("a-101", "a-104")

    assert(actualSkus === expectedSkus)
  }
}
