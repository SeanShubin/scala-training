package com.seanshubin.scala.training.web

import com.seanshubin.scala.training.core.file_system.{FileSystem, FileSystemImpl}
import com.seanshubin.scala.training.core.{DataLoader, DataLoaderImpl, ItemCatalogCoreWiring}
import com.seanshubin.scala.training.web.http.client.{HttpClient, HttpClientImpl}
import com.seanshubin.scala.training.web.http.server.{HandlerWrapper, HttpServer, JettyServer}
import org.eclipse.jetty.server.Handler

trait ItemCatalogWebWiring extends ItemCatalogCoreWiring {
  def itemDataFileDirectory: String

  def itemDataFileName: String

  def host: String

  def port: Int

  lazy val charsetName: String = "UTF-8"
  lazy val newLineSeparator = "\n"
  lazy val newLineSplitPattern = "\r\n|\r|\n"
  lazy val emitLine: String => Unit = println
  lazy val notifications: HttpNotifications = new LineEmittingHttpNotifications(emitLine, newLineSplitPattern)
  lazy val simplifiedHandler: SimplifiedHandler = new SearchHandlerImpl(charsetName, newLineSeparator, searcher, notifications)
  lazy val handler: Handler = new HandlerWrapper(simplifiedHandler)
  lazy val httpServer: HttpServer = new JettyServer(port, handler)
  lazy val httpClient: HttpClient = new HttpClientImpl(host, port, charsetName)
  lazy val fileSystem: FileSystem = new FileSystemImpl(charsetName)
  lazy val dataLoader: DataLoader = new DataLoaderImpl(fileSystem, importer, itemDataFileDirectory, itemDataFileName)
  lazy val smokeTest: SmokeTest = new SmokeTestImpl(smokeTestSupport, httpClient, newLineSeparator, notifications)
}
