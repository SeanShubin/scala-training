package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

class CollectionSuite extends FunSuite {
  test("sequences are for when you can have duplicates and order matters") {
    //if you are not sure which one to use, use a Seq
    //if you have specific performance requirements, use a specific implementation, such as
    //List, IndexedSeq, Stream, Array, Buffer, Range
    val seqA: Seq[Int] = Seq(1, 2, 3)
    val seqB: Seq[Int] = Seq(4, 5, 6)

    //add to the end
    assert(seqA :+ 4 === Seq(1, 2, 3, 4))

    //add to the beginning
    assert(5 +: seqA === Seq(5, 1, 2, 3))

    //concatenate two sequences
    assert(seqA ++ seqB === Seq(1, 2, 3, 4, 5, 6))

    //concatenate a mix of sequences and elements
    assert(Seq(7) ++ seqA ++ Seq(8) ++ seqB ++ Seq(9) === Seq(7, 1, 2, 3, 8, 4, 5, 6, 9))

    //get a copy of a sequence with one element changed
    assert(seqA.updated(1, 99) === Seq(1, 99, 3))

    //apply a function to each element of a seq
    def timesTwo(x: Int): Int = x * 2

    val mapUsingMap = seqA.map(timesTwo)
    val mapUsingFor = for (item <- seqA) yield timesTwo(item)
    val expectedMapResult = Seq(2, 4, 6)
    assert(mapUsingMap === expectedMapResult)
    assert(mapUsingFor === expectedMapResult)

    //apply a filter to a sequence
    def notTwo(x: Int): Boolean = x != 2
    val filterUsingFilter = seqA.filter(notTwo)
    val filterUsingFor = for (item <- seqA; if notTwo(item)) yield item
    val expectedFilterResult = Seq(1, 3)
    assert(filterUsingFilter === expectedFilterResult)
    assert(filterUsingFor === expectedFilterResult)

  }
  test("sets are for when you can't have duplicates and order does not matter") {
    val setA: Set[Int] = Set(1, 2, 3)
    val setB: Set[Int] = Set(-2, -1, 0, 1, 2)

    //add to set
    assert(setA + 4 === Set(1, 2, 3, 4))

    //remove from a set
    assert(setA - 2 === Set(1, 3))

    //union of two sets
    assert(setA ++ setB === Set(-2, -1, 0, 1, 2, 3))

    //difference of two sets
    assert(setA -- setB === Set(3))
    assert(setB -- setA === Set(-2, -1, 0))

    //apply a function to each element of a set
    def squared(x: Int): Int = x * x
    val usingMap = setB.map(squared)
    val usingFor = for (item <- setB) yield squared(item)
    val expected = Set(0, 1, 4)
    assert(usingMap === expected)
    assert(usingFor === expected)
  }
  test("maps are for key value pairs") {
    val mapA: Map[Int, String] = Map(1 -> "a", 2 -> "b", 3 -> "c")

    //the arrow '->' operator is syntax sugar for a tuple2
    val mapB = Map((1, "a"), (2, "b"), (3, "c"))
    assert(mapB === mapA)

    //you can easily convert a sequence of tuple2's to a map
    val seqA = Seq((1, "a"), (2, "b"), (3, "c"))
    val mapC = seqA.toMap
    assert(mapC === mapA)

    //you can also convert a map to a sequence of tuple2's
    val seqB = mapA.toSeq
    assert(seqB === seqA)

    //adding an entry
    val mapD = mapA + (4 -> "d")
    val expectedMapD = Map(1 -> "a", 2 -> "b", 3 -> "c", 4 -> "d")
    assert(mapD === expectedMapD)

    //replacing an entry
    val mapE = mapA + (2 -> "e")
    val expectedMapE = Map(1 -> "a", 2 -> "e", 3 -> "c")
    assert(mapE === expectedMapE)

    //when combining maps, values added later override existing values
    val mapF = Map(1 -> "a", 2 -> "b", 3 -> "c")
    val mapG = Map(3 -> "d", 4 -> "e", 5 -> "f")
    val mapH = mapF ++ mapG
    val expectedMapH = Map(1 -> "a", 2 -> "b", 3 -> "d", 4 -> "e", 5 -> "f")
    assert(mapH === expectedMapH)

    //you can assign default values to maps
    val mapI = mapA.withDefaultValue("abc")
    assert("b" === mapI(2))
    assert("abc" === mapI(4))

    //you can assign default values when you lookup a map value by its key
    assert("b" === mapA.getOrElse(2, "abc"))
    assert("abc" === mapA.getOrElse(4, "abc"))

    //you can iterate over key value pairs
    val seqC = for ((key, value) <- mapA) yield "" + key + "-" + value
    assert(seqC === Seq("1-a", "2-b", "3-c"))

    //you can also iterate using a the .map function, although the syntax to extract the tuple2 is a bit odd
    val seqD = mapA.map {
      case (key, value) => "" + key + "-" + value
    }
    assert(seqD === Seq("1-a", "2-b", "3-c"))

    //you can sidestep the weird syntax and still use the .map function by defining your own mapping function
    def composeEntry(entry: (Int, String)) = {
      val (key, value) = entry
      "" + key + "-" + value
    }
    val seqE = mapA.map(composeEntry)
    assert(seqE === Seq("1-a", "2-b", "3-c"))
  }
}
