package com.seanshubin.scala.training.web

case class SimplifiedResponse(body: String,
                              contentType: String,
                              characterEncoding: String,
                              status: HttpResponseCode)
