package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

class TuplesAndExtractorsSuite extends FunSuite {
  test("construct tuple and pull out all values with extractor") {
    //you can combine multiple values into a tuple
    val a: (Int, String, Double) = (123, "abc", 4.56)

    //you can pull values out of a tuple with an 'extractor' (called destructuring in clojure)
    //where the tuple puts multiple values together, an extractor takes them apart
    //this is signified by the same syntax as creating a tuple, only on the other side of the equals sign, like so
    val (b, c, d) = a

    assert(b === 123)
    assert(c === "abc")
    assert(d === 4.56)
  }

  test("construct tuple and pull out all values by position") {
    //you can combine multiple values into a tuple
    val a: (Int, String, Double) = (123, "abc", 4.56)

    //you can pull out a tuple element by its position, but I suggest that the 'extractor' style is more readable
    val b: Int = a._1
    val c: String = a._2
    val d: Double = a._3

    assert(b === 123)
    assert(c === "abc")
    assert(d === 4.56)
  }

  test("construct tuple and pull out only some values") {
    //you can combine multiple values into a tuple
    val a: (Int, String, Double) = (123, "abc", 4.56)

    //if you only care about specific values, you can use an underscore to indicate what you don't care about
    val (_, b, _) = a

    assert(b === "abc")
  }
}
