package com.seanshubin.scala.training.core

trait DataStorage {
  def notThreadSafeAddItems(items: Iterable[Item])

  def searchByColorAndName(color: String, name: String): Seq[Item]

  def searchByName(name: String): Seq[Item]

  def findAnyItemWithColor(): Item
}
