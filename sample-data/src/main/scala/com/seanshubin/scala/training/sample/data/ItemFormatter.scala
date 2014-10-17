package com.seanshubin.scala.training.sample.data

import com.seanshubin.scala.training.core.Item

trait ItemFormatter {
  def format(item: Item): String
}
