package com.seanshubin.scala.training.web.http.client

import com.seanshubin.scala.training.web.HttpResponseCode

trait HttpClient {
  def get(path: String, parameterKey: String, parameterValue: String): (HttpResponseCode, String)
}
