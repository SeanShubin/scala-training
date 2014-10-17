package com.seanshubin.scala.training.web

import com.seanshubin.scala.training.web.exception_info.ExceptionInfo

class LineEmittingHttpNotifications(emit: String => Unit, newLineSplitPattern: String) extends HttpNotifications {
  def incomingRequest(request: SimplifiedRequest) {
    val SimplifiedRequest(target, method, parameterMap) = request
    val parameterMapString = parameterMapToString(parameterMap)
    emit(s"request: $target $method $parameterMapString")
  }

  def outgoingResponse(response: SimplifiedResponse) {
    val SimplifiedResponse(body, _, _, status) = response
    val lines: Seq[String] = if (body == "") Seq() else body.split(newLineSplitPattern)
    val lineCount = lines.size
    emit(s"response: status = ${status.code} (${status.name}), line count = $lineCount")
    def indentLine(line: String): String = "  " + line
    val indentedLines = lines.map(indentLine)
    indentedLines.foreach(emit)
  }

  def smokeTestStarted(name: String) {
    emit(s"Smoke test started: $name")
  }

  def smokeTestSucceeded(name: String) {
    emit(s"SUCCESS: $name")
  }

  def smokeTestFailed(name: String, message: String) {
    emit(s"FAILURE")
    emit(s"  name   = $name")
    emit(s"  reason = $message")
  }

  def unhandledException(request: SimplifiedRequest, exceptionInfo: ExceptionInfo) {
    val requestLines = request.toMultipleLineString
    val exceptionLines = exceptionInfo.toMultipleLineString
    val linesToEmit = requestLines ++ exceptionLines
    linesToEmit.foreach(emit)
  }

  private def parameterMapToString(parameterMap: Map[String, Seq[String]]): String = {
    parameterMap.map(parameterMapEntryToString).mkString(";")
  }

  private def parameterMapEntryToString(entry: (String, Seq[String])): String = {
    val (key, values) = entry
    val valuesString: String = values.mkString(",")
    s"$key=$valuesString"
  }
}
