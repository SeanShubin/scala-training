package com.seanshubin.scala.training.web

import com.seanshubin.scala.training.web.exception_info.{ExceptionInfo, StackTraceElementInfo}
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.mutable.ArrayBuffer

class HttpNotificationsSuite extends FunSuite with BeforeAndAfter {
  val newLineSplitPattern = "\r\n|\r|\n"
  val actual: ArrayBuffer[String] = new ArrayBuffer[String]()

  def emit(line: String) {
    actual += line
  }

  def assertSequencesEqual(actualSeq: Seq[String], expectedSeq: Seq[String]) {
    def assertListsEqual(lineNumber: Int, remainingActual: List[String], remainingExpected: List[String]) {
      (remainingActual.headOption, remainingExpected.headOption) match {
        case (Some(actualLine), Some(expectedLine)) =>
          assert(actualLine === expectedLine, actualSeq.take(lineNumber).mkString("\n"))
          assertListsEqual(lineNumber + 1, remainingActual.tail, remainingExpected.tail)
        case (Some(actualLine), None) => fail((expectedSeq :+ s"extra line: $actualLine").mkString("\n"))
        case (None, Some(expectedLine)) => fail((actualSeq :+ s"missing line: $expectedLine").mkString("\n"))
        case (None, None) =>
      }
    }
    assertListsEqual(0, actualSeq.toList, expectedSeq.toList)
  }

  val notifications: HttpNotifications = new LineEmittingHttpNotifications(emit, newLineSplitPattern)

  before {
    actual.clear()
  }

  test("incoming request") {
    val request: SimplifiedRequest = SimplifiedRequest(
      "/target",
      "method",
      Map(
        "key-1" -> Seq("value-1", "value-2"),
        "key-2" -> Seq("value-3", "value-4")
      ))
    notifications.incomingRequest(request)
    val expected = Seq("request: /target method key-1=value-1,value-2;key-2=value-3,value-4")
    assert(actual === expected)
  }

  test("outgoing response") {
    val response: SimplifiedResponse = SimplifiedResponse("some-body", "some-content-type", "some-character-encoding", HttpResponseCode.Ok)
    notifications.outgoingResponse(response)
    val expected = Seq(
      "response: status = 200 (Ok), line count = 1",
      "  some-body")
    assert(actual === expected)
  }

  test("smoke test started") {
    val smokeTestName = "smoke-test-name"
    notifications.smokeTestStarted(smokeTestName)
    val expected = Seq("Smoke test started: smoke-test-name")
    assert(actual === expected)
  }

  test("smoke test succeeded") {
    val smokeTestName = "smoke-test-name"
    notifications.smokeTestSucceeded(smokeTestName)
    val expected = Seq("SUCCESS: smoke-test-name")
    assert(actual === expected)
  }

  test("smoke test failed") {
    val smokeTestName = "smoke-test-name"
    val message = "message"
    notifications.smokeTestFailed(smokeTestName, message)
    val expected = Seq(
      "FAILURE",
      "  name   = smoke-test-name",
      "  reason = message")
    assert(actual === expected)
  }

  test("unhandled exception") {
    val request: SimplifiedRequest = SimplifiedRequest(
      "/target",
      "method",
      Map(
        "key-1" -> Seq("value-1", "value-2"),
        "key-2" -> Seq("value-3", "value-4")
      ))

    val stackTrace = Seq(
      StackTraceElementInfo("class-a", "method-a", "filename-a", 123),
      StackTraceElementInfo("class-b", "method-b", "filename-b", 234)
    )
    val rootStackTrace = Seq(
      StackTraceElementInfo("class-c", "method-c", "filename-c", 345),
      StackTraceElementInfo("class-d", "method-d", "filename-d", 456)
    )
    val cause = ExceptionInfo("java.lang.RuntimeException: root problem", None, rootStackTrace)
    val exceptionInfo = ExceptionInfo("java.lang.RuntimeException: oops!", Some(cause), stackTrace)
    notifications.unhandledException(request, exceptionInfo)
    val expected = Seq(
      "target = /target",
      "method = method",
      "parameters:",
      "  key-1 = value-1",
      "  key-1 = value-2",
      "  key-2 = value-3",
      "  key-2 = value-4",
      "java.lang.RuntimeException: oops!",
      "    class-a.method-a(filename-a:123)",
      "    class-b.method-b(filename-b:234)",
      "java.lang.RuntimeException: root problem",
      "    class-c.method-c(filename-c:345)",
      "    class-d.method-d(filename-d:456)"
    )
    assertSequencesEqual(actual, expected)
  }
}
