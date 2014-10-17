package com.seanshubin.scala.training.core

class SearcherImpl(queryParser: QueryParser, dataStorage: DataStorage) extends Searcher {
  def queryForSkus(query: String): Seq[String] = {
    queryParser.parse(query) match {
      case ColorAndNameQuery(color, name) =>
        val items = dataStorage.searchByColorAndName(color, name)
        val skus = items.map(extractSku)
        skus
      case NameQuery(name) =>
        val items = dataStorage.searchByName(name)
        val skus = items.map(extractSku)
        skus
    }
  }

  private def extractSku(item: Item) = item.sku
}
