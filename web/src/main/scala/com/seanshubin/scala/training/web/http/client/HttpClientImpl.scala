package com.seanshubin.scala.training.web.http.client

import java.io.ByteArrayOutputStream

import com.seanshubin.scala.training.web.HttpResponseCode
import org.apache.http.client.methods.{HttpGet, HttpRequestBase}
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.DefaultHttpClient

class HttpClientImpl(host: String, port: Int, charsetName: String) extends HttpClient {
  private val scheme = "http"

  def get(path: String, parameterKey: String, parameterValue: String): (HttpResponseCode, String) = {
    val httpClient = new DefaultHttpClient()
    val uri = new URIBuilder().setScheme(scheme).setHost(host).setPort(port).setPath(path).addParameter(parameterKey, parameterValue).build()
    val httpRequestBase: HttpRequestBase = new HttpGet(uri)
    val response = httpClient.execute(httpRequestBase)
    val statusCode = response.getStatusLine.getStatusCode
    val byteArrayOutputStream = new ByteArrayOutputStream()
    response.getEntity.writeTo(byteArrayOutputStream)
    val body = new String(byteArrayOutputStream.toByteArray, charsetName)
    (HttpResponseCode.fromCode(statusCode), body)
  }
}
