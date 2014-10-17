package com.seanshubin.scala.training.sample.data

import com.seanshubin.scala.training.core.Item

class ItemFormatterImpl extends ItemFormatter {
  def format(item: Item): String = {
    item match {
      case Item(Some(color), name, sku) => s"$color $name with sku $sku"
      case Item(None, name, sku) => s"$name with sku $sku"
    }
  }
}
