package com.seanshubin.scala.training.core.unit_tests

import com.seanshubin.scala.training.core.{DataStorage, InMemoryDataStorage, Item}
import org.scalatest.FunSuite

class DataStorageSuite extends FunSuite {
  test("search by color and name") {
    val data: DataStorage = new InMemoryDataStorage

    val searchColor = "red"
    val searchName = "bike"

    val differentColorAndName = Item("blue", "motorcycle", "dont-find-1")
    val sameColorAndName1 = Item("red", "bike", "find-1")
    val sameName = Item("blue", "bike", "dont-find-2")
    val sameColorAndName2 = Item("red", "bike", "find-2")
    val sameColor = Item("red", "motorcycle", "dont-find-3")

    val items = Seq(
      differentColorAndName,
      sameColorAndName1,
      sameName,
      sameColorAndName2,
      sameColor)

    data.notThreadSafeAddItems(items)
    val foundItems = data.searchByColorAndName(searchColor, searchName)

    assert(foundItems.size === 2)
    assert(foundItems.contains(sameColorAndName1))
    assert(foundItems.contains(sameColorAndName2))
  }

  test("search by name") {
    val data: DataStorage = new InMemoryDataStorage

    val searchName = "bike"

    val sameName1 = Item("red", "bike", "find-1")
    val sameName2 = Item("blue", "bike", "find-2")
    val sameNameNoColor = Item("bike", "find-3")
    val differentName1 = Item("red", "motorcycle", "dont-find-1")
    val differentName2 = Item("blue", "motorcycle", "dont-find-2")
    val differentNameNoColor = Item("motorcycle", "dont-find-3")

    val items = Seq(
      sameName1,
      sameName2,
      sameNameNoColor,
      differentName1,
      differentName2,
      differentNameNoColor)

    data.notThreadSafeAddItems(items)
    val foundItems = data.searchByName(searchName)

    assert(foundItems.size === 3)
    assert(foundItems.contains(sameName1))
    assert(foundItems.contains(sameName2))
    assert(foundItems.contains(sameNameNoColor))
  }
}
