package com.seanshubin.scala.training.core

trait QueryParser {
  def parse(queryString: String): Query
}
