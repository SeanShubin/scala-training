package com.seanshubin.scala.training.core

case class Item(maybeColor: Option[String], name: String, sku: String)

object Item {
  def apply(color: String, name: String, sku: String) = new Item(Some(color), name, sku)

  def apply(name: String, sku: String) = new Item(None, name, sku)
}
