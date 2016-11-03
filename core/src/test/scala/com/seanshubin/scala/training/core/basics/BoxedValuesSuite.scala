package com.seanshubin.scala.training.core.basics

import java.lang.{Long => BoxedLong}
import java.util

import org.scalatest.FunSuite

import scala.collection.JavaConverters._

class BoxedValuesSuite extends FunSuite {
  def javaStyleReverse(values: Array[Long]): Array[Long] = {
    val boxedValues: util.List[BoxedLong] = new util.ArrayList[BoxedLong]
    for (value <- values) {
      boxedValues.add(value)
    }
    util.Collections.reverse(boxedValues)
    val reversedValues: Array[Long] = new Array[Long](values.length)
    for {i <- values.indices} {
      reversedValues(i) = boxedValues.get(i)
    }
    reversedValues
  }

  def javaStyleReverse(values: util.Collection[BoxedLong]): util.Collection[BoxedLong] = {
    val list: util.List[BoxedLong] = new util.ArrayList[BoxedLong](values)
    util.Collections.reverse(list)
    list
  }

  test("arrays of primitives") {
    val values: Seq[Long] = Seq(1, 2, 3)

    val reversedUsingArray: Seq[Long] = javaStyleReverse(values.toArray)
    assert(reversedUsingArray === Seq(3, 2, 1))
  }

  //It is important to understand all of the java converters before choosing one
  //In particular, you need to know which ones return the original object when converting back and forth
  //Read the documentation here
  //http://www.scala-lang.org/api/current/#scala.collection.JavaConverters$
  test("java collections") {
    val values: Seq[Long] = Seq(1, 2, 3)

    //use valueOf to box
    val boxed: Seq[BoxedLong] = values.map(BoxedLong.valueOf)
    val collection: util.Collection[BoxedLong] = boxed.asJava
    val reversedCollection: util.Collection[BoxedLong] = javaStyleReverse(collection)
    val reversedBoxed: Seq[BoxedLong] = reversedCollection.asScala.toSeq
    //use longValue to unbox
    val reversedUsingCollection: Seq[Long] = reversedBoxed.map(_.longValue())
    assert(reversedUsingCollection === Seq(3, 2, 1))

    //same as above, only in one statement
    val reversedUsingCollectionOneStatement: Seq[Long] =
    javaStyleReverse(values.map(BoxedLong.valueOf).asJava).asScala.map(_.longValue()).toSeq
    assert(reversedUsingCollectionOneStatement === Seq(3, 2, 1))
  }
}
