package com.seanshubin.scala.training.web

trait SimplifiedHandler {
  def handle(request: SimplifiedRequest): SimplifiedResponse
}
