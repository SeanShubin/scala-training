package com.seanshubin.scala.training.core.basics

import com.seanshubin.scala.training.core.lines.LinesCompare
import org.scalatest.FunSuite

class MapFlatMapForYield extends FunSuite {
  test("map times 2") {
    val someNumbers = Seq(1, 2, 3, 4, 5)
    def timesTwo(x: Int) = x * 2
    val someNumbersTimesTwo_A = someNumbers.map(timesTwo)
    val someNumbersTimesTwo_B = someNumbers.map(x => x * 2)
    val someNumbersTimesTwo_C = someNumbers.map(_ * 2)
    val someNumbersTimesTwo_D = for (number <- someNumbers) yield number * 2
    assert(someNumbersTimesTwo_A === Seq(2, 4, 6, 8, 10))
    assert(someNumbersTimesTwo_B === Seq(2, 4, 6, 8, 10))
    assert(someNumbersTimesTwo_C === Seq(2, 4, 6, 8, 10))
    assert(someNumbersTimesTwo_D === Seq(2, 4, 6, 8, 10))
  }

  test("map squared") {
    val someNumbers = Seq(1, 2, 3, 4, 5)
    def square(x: Int) = x * x
    val someNumbersSquared_A = someNumbers.map(square)
    val someNumbersSquared_B = someNumbers.map(x => x * x)
    val someNumbersSquared_C = for (number <- someNumbers) yield number * number
    assert(someNumbersSquared_A === Seq(1, 4, 9, 16, 25))
    assert(someNumbersSquared_B === Seq(1, 4, 9, 16, 25))
    assert(someNumbersSquared_C === Seq(1, 4, 9, 16, 25))
  }

  test("flatten") {
    val someSequences = Seq(Seq(4, 1, 8), Seq(5, 9, 3), Seq(7, 2, 6))
    val expected = Seq(4, 1, 8, 5, 9, 3, 7, 2, 6)
    val actual = someSequences.flatten
    assert(actual === expected)
  }

  test("flat map") {
    val someSequences = Seq(Seq(4, 1, 8), Seq(5, 9, 3), Seq(7, 2, 6))
    def timesTwo(x: Int) = x * 2
    def seqTimesTwo(seq: Seq[Int]) = seq.map(timesTwo)
    val someNumbersTimesTwo_A = someSequences.flatMap(seqTimesTwo)
    val someNumbersTimesTwo_B = someSequences.map(seqTimesTwo).flatten
    val someNumbersTimesTwo_C = for {
      sequence <- someSequences
      number <- sequence
    } yield timesTwo(number)
    assert(someNumbersTimesTwo_A === Seq(8, 2, 16, 10, 18, 6, 14, 4, 12))
    assert(someNumbersTimesTwo_B === Seq(8, 2, 16, 10, 18, 6, 14, 4, 12))
    assert(someNumbersTimesTwo_C === Seq(8, 2, 16, 10, 18, 6, 14, 4, 12))
  }

  test("for with index 1") {
    val cats = Seq("Chinese Mountain", "Domestic", "Jungle", "Pallas", "Sand", "Black Footed", "Wild")

    val actual = for (index <- 0 to cats.size - 1) yield {
      s"index = $index, cat = ${cats(index)}"
    }

    val expected =
      """index = 0, cat = Chinese Mountain
        |index = 1, cat = Domestic
        |index = 2, cat = Jungle
        |index = 3, cat = Pallas
        |index = 4, cat = Sand
        |index = 5, cat = Black Footed
        |index = 6, cat = Wild"""

    verifyLines(actual, expected)
  }

  test("for with index 2") {
    val cats = Seq("Chinese Mountain", "Domestic", "Jungle", "Pallas", "Sand", "Black Footed", "Wild")
    val indexedCats = (0 to cats.size - 1) zip cats
    val actual = for ((index, cat) <- indexedCats) yield {
      s"index = $index, cat = $cat"
    }
    val expected =
      """index = 0, cat = Chinese Mountain
        |index = 1, cat = Domestic
        |index = 2, cat = Jungle
        |index = 3, cat = Pallas
        |index = 4, cat = Sand
        |index = 5, cat = Black Footed
        |index = 6, cat = Wild"""

    verifyLines(actual, expected)
  }

  test("for with index 3") {
    val cats = Seq("Chinese Mountain", "Domestic", "Jungle", "Pallas", "Sand", "Black Footed", "Wild")
    val indexedCats = cats.zipWithIndex
    val actual = for ((cat, index) <- indexedCats) yield {
      s"index = $index, cat = $cat"
    }
    val expected =
      """index = 0, cat = Chinese Mountain
        |index = 1, cat = Domestic
        |index = 2, cat = Jungle
        |index = 3, cat = Pallas
        |index = 4, cat = Sand
        |index = 5, cat = Black Footed
        |index = 6, cat = Wild"""

    verifyLines(actual, expected)
  }

  test("for yield 1") {
    val actual = for (
      i <- 1 to 5; //generator
      j <- i + 1 to 5; //generator
      k <- j + 1 to 5; //generator
      tuple = (i, j, k); //definition
      sum = i + j + k; //definition
      product = i * j * k; //definition
      if (product != 10); //filter (skips the tuple 1,2,5)
      if (sum != 10)) yield {
      //filter (skips the tuple 1,4,5)
      s"$tuple sum is $sum, product is $product"
    }
    val expected =
      """(1,2,3) sum is 6, product is 6
        |(1,2,4) sum is 7, product is 8
        |(1,3,4) sum is 8, product is 12
        |(1,3,5) sum is 9, product is 15
        |(2,3,4) sum is 9, product is 24
        |(2,4,5) sum is 11, product is 40
        |(3,4,5) sum is 12, product is 60"""

    verifyLines(actual, expected)
  }

  test("for yield 2") {
    val forResults = for (i <- 1 to 5) yield i * i
    val mapResults = (1 to 5).map(i => i * i)

    assert(forResults === Seq(1, 4, 9, 16, 25))
    assert(mapResults === Seq(1, 4, 9, 16, 25))
  }

  test("for yield 3") {
    val forResults = for (i <- 1 to 10; if (i % 2 == 1)) yield i * i
    val mapResults = (1 to 10).withFilter(i => i % 2 == 1).map(i => i * i)

    assert(forResults === Seq(1, 9, 25, 49, 81))
    assert(mapResults === Seq(1, 9, 25, 49, 81))
  }

  test("for yield 4") {
    val forResults1 = for (
      i <- 1 to 5;
      j <- i + 1 to 5;
      k <- j + 1 to 5) yield {
      (i, j, k)
    }

    val mapResults1 = (1 to 5).flatMap(i => for (
      j <- i + 1 to 5;
      k <- j + 1 to 5) yield {
      (i, j, k)
    })

    val mapResults2 = (1 to 5).flatMap(i => (i + 1 to 5).flatMap(j => for (
      k <- j + 1 to 5) yield {
      (i, j, k)
    }))

    val mapResults3 = (1 to 5).flatMap(i =>
      (i + 1 to 5).flatMap(j =>
        (j + 1 to 5).map(k =>
          (i, j, k)
        )))

    assert(forResults1.mkString(" ") === "(1,2,3) (1,2,4) (1,2,5) (1,3,4) (1,3,5) (1,4,5) (2,3,4) (2,3,5) (2,4,5) (3,4,5)")
    assert(mapResults1.mkString(" ") === "(1,2,3) (1,2,4) (1,2,5) (1,3,4) (1,3,5) (1,4,5) (2,3,4) (2,3,5) (2,4,5) (3,4,5)")
    assert(mapResults2.mkString(" ") === "(1,2,3) (1,2,4) (1,2,5) (1,3,4) (1,3,5) (1,4,5) (2,3,4) (2,3,5) (2,4,5) (3,4,5)")
    assert(mapResults3.mkString(" ") === "(1,2,3) (1,2,4) (1,2,5) (1,3,4) (1,3,5) (1,4,5) (2,3,4) (2,3,5) (2,4,5) (3,4,5)")
  }

  def verifyLines(actual: Seq[String], expectedString: String) {
    val expected = expectedString.stripMargin.split("\r\n|\r|\n")
    val compareResult = LinesCompare.compareLines(actual, expected)
    assert(compareResult.areSame, compareResult.toMultipleLineString.mkString("\n"))
  }

  /*
  option
  future
  either
   */
}
