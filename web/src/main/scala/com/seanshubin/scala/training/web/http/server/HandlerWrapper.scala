package com.seanshubin.scala.training.web.http.server

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.seanshubin.scala.training.web.{MoreJavaConversions, SimplifiedHandler, SimplifiedRequest, SimplifiedResponse}
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler

class HandlerWrapper(simplifiedHandler: SimplifiedHandler) extends AbstractHandler {
  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
    val parameterMap = MoreJavaConversions.toScalaMap(request.getParameterMap)
    val simplifiedRequest = SimplifiedRequest(target, request.getMethod, parameterMap)
    val SimplifiedResponse(body, contentType, characterEncoding, status) = simplifiedHandler.handle(simplifiedRequest)
    val encodedResponseBody = body.getBytes(characterEncoding)
    response.setContentType(contentType)
    response.setCharacterEncoding(characterEncoding)
    response.setStatus(status.code)
    response.getOutputStream.write(encodedResponseBody)
    response.getOutputStream.flush()
    baseRequest.setHandled(true)
  }
}
