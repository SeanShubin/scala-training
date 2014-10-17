package com.seanshubin.scala.training.core

class InMemoryDataStorage extends DataStorage {
  private var notThreadSafeItems: Seq[Item] = Seq()

  def searchByColorAndName(color: String, name: String): Seq[Item] = {
    def colorMatches(item: Item) = item.maybeColor == Some(color)
    def nameMatches(item: Item) = item.name == name
    notThreadSafeItems.filter(colorMatches).filter(nameMatches)
  }

  def searchByName(name: String): Seq[Item] = {
    def nameMatches(item: Item) = item.name == name
    notThreadSafeItems.filter(nameMatches).filter(nameMatches)
  }

  def notThreadSafeAddItems(items: Iterable[Item]) {
    notThreadSafeItems = notThreadSafeItems ++ items.toSeq
  }

  private def hasColor(item: Item) = item.maybeColor.isDefined

  def findAnyItemWithColor(): Item = notThreadSafeItems.filter(hasColor).head
}
