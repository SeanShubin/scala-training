package com.seanshubin.scala.training.sample.data

import com.seanshubin.scala.training.core.Item

class ItemLineGeneratorImpl(itemGenerator: ItemGenerator, itemCount: Int, itemFormatter: ItemFormatter) extends ItemLineGenerator {
  def generateItemLines(): Seq[String] = {
    val items: Seq[Item] = itemGenerator.generate(itemCount)
    val lines: Seq[String] = items.map(itemFormatter.format)
    lines
  }
}
