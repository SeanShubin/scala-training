package com.seanshubin.scala.training.web

import com.seanshubin.scala.training.core.Searcher
import com.seanshubin.scala.training.web.exception_info.ExceptionInfo
import org.scalatest.easymock.EasyMockSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

class SearchRequestHandlerSuite extends FunSuite with EasyMockSugar with BeforeAndAfter {
  val characterEncoding = "utf-8"
  val newLineSeparator = "\n"

  var searcher: Searcher = _
  var notifications: HttpNotifications = _
  var searchHandler: SimplifiedHandler = _

  def usingMocks(codeToTest: => Unit) {
    whenExecuting(searcher, notifications)(codeToTest)
  }

  before {
    searcher = mock[Searcher]
    notifications = mock[HttpNotifications]
    searchHandler = new SearchHandlerImpl(
      characterEncoding, newLineSeparator, searcher, notifications)
  }

  test("valid search request") {
    val request = SimplifiedRequest(
      target = "/search",
      method = "GET",
      parameterMap = Map("query" -> Seq("black socks"))
    )

    val expected = SimplifiedResponse("sku-1\nsku-2", "text/plain", "utf-8", HttpResponseCode.Ok)

    expecting {
      searcher.queryForSkus("black socks").andReturn(Seq("sku-1", "sku-2"))
      notifications.incomingRequest(request)
      notifications.outgoingResponse(expected)
    }

    usingMocks {
      val actual = searchHandler.handle(request)
      assert(actual === expected)
    }
  }

  test("unsupported") {
    val request = SimplifiedRequest(
      target = "/unsupported",
      method = "POST",
      parameterMap = Map("query" -> Seq("black socks"))
    )

    val expectedBody =
      """unsupported request type
        |target = /unsupported
        |method = POST
        |parameters:
        |  query = black socks""".stripMargin.replaceAll( """\r\n|\r""", "\n")
    val expected = SimplifiedResponse(expectedBody, "text/plain", "utf-8", HttpResponseCode.BadRequest)

    expecting {
      notifications.incomingRequest(request)
      notifications.outgoingResponse(expected)
    }

    usingMocks {
      val actual = searchHandler.handle(request)
      assert(actual === expected)
    }
  }

  test("exception") {
    val expectedRequest = SimplifiedRequest(
      target = "/search",
      method = "GET",
      parameterMap = Map("query" -> Seq("black socks"))
    )

    val exception = new RuntimeException("Ouchie!")
    val expectedExceptionInfo = ExceptionInfo.fromException(exception)

    val expectedBody =
      """server error
        |target = /search
        |method = GET
        |parameters:
        |  query = black socks""".stripMargin.replaceAll( """\r\n|\r""", "\n")
    val expectedResponse = SimplifiedResponse(expectedBody, "text/plain", "utf-8", HttpResponseCode.InternalServerError)

    //have to use a fake to simulate an exception because mocks mess with the stack trace
    val fakeSearcher: Searcher = new Searcher {
      def queryForSkus(query: String) = {
        assert(query === "black socks")
        throw exception
      }
    }

    val fakeNotifications: HttpNotifications = new FakeNotifications {
      override def incomingRequest(actualRequest: SimplifiedRequest) {
        assert(actualRequest == expectedRequest)
      }

      override def outgoingResponse(actualResponse: SimplifiedResponse) {
        assert(actualResponse === expectedResponse)
      }

      override def unhandledException(actualRequest: SimplifiedRequest, actualExceptionInfo: ExceptionInfo) {
        assert(actualRequest === expectedRequest)
        assert(actualExceptionInfo === expectedExceptionInfo)
      }
    }

    val searchHandler = new SearchHandlerImpl(
      characterEncoding, newLineSeparator, fakeSearcher, fakeNotifications)

    val actual = searchHandler.handle(expectedRequest)
    assert(actual === expectedResponse)
  }

  test("bad search query") {
    val request = SimplifiedRequest(
      target = "/search",
      method = "GET",
      parameterMap = Map("blah" -> Seq("blah", "blah"))
    )

    val expected = SimplifiedResponse("/search requires exactly one 'query' parameter", "text/plain", "utf-8", HttpResponseCode.BadRequest)

    expecting {
      notifications.incomingRequest(request)
      notifications.outgoingResponse(expected)
    }

    usingMocks {
      val actual = searchHandler.handle(request)
      assert(actual === expected)
    }
  }
}
