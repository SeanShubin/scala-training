package com.seanshubin.scala.training.core

class SmokeTestSupportImpl(data: DataStorage) extends SmokeTestSupport {
  def findAnyItemWithColor(): Item = data.findAnyItemWithColor()
}
