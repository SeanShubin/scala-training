package com.seanshubin.scala.training.core

trait Searcher {
  def queryForSkus(query: String): Seq[String]
}
