package com.seanshubin.scala.training.sample.data

import com.seanshubin.scala.training.core.Item

trait ItemGenerator {
  def generate(howMany: Int): Seq[Item]
}
