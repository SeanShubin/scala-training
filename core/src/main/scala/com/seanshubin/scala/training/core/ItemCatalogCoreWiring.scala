package com.seanshubin.scala.training.core

trait ItemCatalogCoreWiring {
  lazy val queryParser: QueryParser = new QueryParserImpl()
  lazy val dataStorage: DataStorage = new InMemoryDataStorage()
  lazy val searcher: Searcher = new SearcherImpl(queryParser, dataStorage)
  lazy val importParser = new ImportParserImpl()
  lazy val importer: Importer = new ImporterImpl(importParser, dataStorage)
  lazy val smokeTestSupport: SmokeTestSupport = new SmokeTestSupportImpl(dataStorage)
}
