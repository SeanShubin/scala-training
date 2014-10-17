package com.seanshubin.scala.training.web

import com.seanshubin.scala.training.core.Searcher
import com.seanshubin.scala.training.web.exception_info.ExceptionInfo

class SearchHandlerImpl(characterEncoding: String,
                        newLineSeparator: String,
                        searcher: Searcher,
                        notifications: HttpNotifications) extends SimplifiedHandler {
  def handle(request: SimplifiedRequest): SimplifiedResponse = {
    notifications.incomingRequest(request)
    val response: SimplifiedResponse = try {
      request match {
        case SimplifiedRequest("/search", "GET", query) => handleSearch(query)
        case SimplifiedRequest("/favicon.ico", "GET", query) => ignoreRequest()
        case _ => handleUnsupported(request)
      }
    } catch {
      case exception: Throwable =>
        handleException(request, exception)
    }
    notifications.outgoingResponse(response)
    response
  }

  private def handleSearch(parameterMap: Map[String, Seq[String]]): SimplifiedResponse = {
    parameterMap.get("query") match {
      case Some(Seq(singleItem)) => handleSearch(singleItem)
      case _ => handleBadSearchQuery()
    }
  }

  private def ignoreRequest(): SimplifiedResponse = {
    SimplifiedResponse("", "text/plain", characterEncoding, HttpResponseCode.NoContent)
  }

  private def handleSearch(query: String): SimplifiedResponse = {
    val skus = searcher.queryForSkus(query)
    val responseBody = skus.mkString(newLineSeparator)
    val contentType = "text/plain"
    val status = HttpResponseCode.Ok
    val simplifiedResponse = SimplifiedResponse(responseBody, contentType, characterEncoding, status)
    simplifiedResponse
  }

  private def handleUnsupported(request: SimplifiedRequest): SimplifiedResponse = {
    val responseLines = "unsupported request type" +: request.toMultipleLineString
    val responseBody = responseLines.mkString(newLineSeparator)
    val contentType = "text/plain"
    val status = HttpResponseCode.BadRequest
    val simplifiedResponse = SimplifiedResponse(responseBody, contentType, characterEncoding, status)
    simplifiedResponse
  }

  private def handleBadSearchQuery(): SimplifiedResponse = {
    val responseBody = "/search requires exactly one 'query' parameter"
    val contentType = "text/plain"
    val status = HttpResponseCode.BadRequest
    val simplifiedResponse = SimplifiedResponse(responseBody, contentType, characterEncoding, status)
    simplifiedResponse
  }

  private def handleException(request: SimplifiedRequest, exception: Throwable): SimplifiedResponse = {
    val responseLines = "server error" +: request.toMultipleLineString
    val responseBody = responseLines.mkString(newLineSeparator)
    val contentType = "text/plain"
    val status = HttpResponseCode.InternalServerError
    val simplifiedResponse = SimplifiedResponse(responseBody, contentType, characterEncoding, status)
    notifications.unhandledException(request, ExceptionInfo.fromException(exception))
    simplifiedResponse
  }
}
