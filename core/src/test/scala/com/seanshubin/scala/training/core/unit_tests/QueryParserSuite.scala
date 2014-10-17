package com.seanshubin.scala.training.core.unit_tests

import com.seanshubin.scala.training.core._
import org.scalatest.FunSuite

class QueryParserSuite extends FunSuite {
  test("parse color and name query") {
    val query = "red car"
    val queryParser: QueryParser = new QueryParserImpl()

    val actualQuery = queryParser.parse(query)

    val expectedColor = "red"
    val expectedName = "car"
    val expectedQuery = ColorAndNameQuery(expectedColor, expectedName)
    assert(expectedQuery === actualQuery)
  }

  test("sensible error message for invalid query string") {
    val query = "some wacky nonsense"
    val queryParser: QueryParser = new QueryParserImpl()

    val error = intercept[Throwable] {
      queryParser.parse(query)
    }

    val actualMessage = error.getMessage
    val expectedMessage = "Unable to parse the text 'some wacky nonsense' into a valid query"
    assert(expectedMessage === actualMessage)
  }

  test("parse name query") {
    val query = "car"
    val queryParser: QueryParser = new QueryParserImpl()

    val actualQuery = queryParser.parse(query)

    val expectedName = "car"
    val expectedQuery = NameQuery(expectedName)
    assert(expectedQuery === actualQuery)
  }
}
