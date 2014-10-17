package com.seanshubin.scala.training.web

import com.seanshubin.scala.training.web.exception_info.ExceptionInfo

trait HttpNotifications {
  def incomingRequest(request: SimplifiedRequest)

  def outgoingResponse(response: SimplifiedResponse)

  def smokeTestStarted(name: String)

  def smokeTestSucceeded(name: String)

  def smokeTestFailed(name: String, message: String)

  def unhandledException(request: SimplifiedRequest, exception: ExceptionInfo)
}
