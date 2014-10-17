package com.seanshubin.scala.training.core

class QueryParserImpl extends QueryParser {
  private val NamePattern = """(\w+)""".r
  private val ColorAndNamePattern = """(\w+) (\w+)""".r

  def parse(queryString: String): Query = queryString match {
    case NamePattern(name) => NameQuery(name)
    case ColorAndNamePattern(color, name) => ColorAndNameQuery(color, name)
    case _ => throw new RuntimeException(s"Unable to parse the text '$queryString' into a valid query")
  }
}
