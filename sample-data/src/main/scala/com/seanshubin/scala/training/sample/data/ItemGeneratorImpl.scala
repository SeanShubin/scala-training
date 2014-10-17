package com.seanshubin.scala.training.sample.data

import com.seanshubin.scala.training.core.Item

class ItemGeneratorImpl(items: Seq[String], colors: Seq[Option[String]], randomChooser: RandomChooser) extends ItemGenerator {
  def generate(howMany: Int): Seq[Item] = (0 until howMany).map(generateItem)

  def generateItem(index: Int): Item = {
    val maybeColor = randomChooser.fromSeq(colors)
    val name = randomChooser.fromSeq(items)
    val sku = maybeColor match {
      case Some(color) => f"sku-$color-$name-$index%02d"
      case None => f"sku-$name-$index%02d"
    }
    Item(maybeColor, name, sku)
  }
}
