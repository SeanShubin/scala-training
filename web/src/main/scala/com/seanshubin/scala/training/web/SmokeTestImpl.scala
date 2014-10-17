package com.seanshubin.scala.training.web

import com.seanshubin.scala.training.core.{Item, SmokeTestSupport}
import com.seanshubin.scala.training.web.http.client.HttpClient

class SmokeTestImpl(smokeTestSupport: SmokeTestSupport,
                    httpClient: HttpClient,
                    newLineSeparator: String,
                    httpNotifications: HttpNotifications) extends SmokeTest {
  def runSmokeTests() {
    makeSureWeCanSearchForAnItemByColorAndName()
  }

  private def makeSureWeCanSearchForAnItemByColorAndName() {
    val smokeTestName = "make sure we can search for an item by color and name"
    httpNotifications.smokeTestStarted(smokeTestName)
    val item: Item = smokeTestSupport.findAnyItemWithColor()
    val Item(Some(color), name, sku) = item
    val query = s"$color $name"
    val (statusCode, responseBody) = httpClient.get("/search", "query", query)
    if (statusCode != HttpResponseCode.Ok) throw new RuntimeException("Smoke test failed!")
    val skus: Seq[String] = responseBody.split(newLineSeparator)
    if (!skus.contains(item.sku)) {
      val message = s"searched for color $color and name $name, but sku $sku not found"
      httpNotifications.smokeTestFailed(smokeTestName, message)
      throw new RuntimeException(s"$smokeTestName: $message")
    }
    httpNotifications.smokeTestSucceeded(smokeTestName)
  }
}
