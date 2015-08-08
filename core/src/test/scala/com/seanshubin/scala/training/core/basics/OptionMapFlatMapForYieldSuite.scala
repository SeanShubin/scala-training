package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

class OptionMapFlatMapForYieldSuite extends FunSuite {
  //demonstrates that map and flatMap and for comprehensions are interchangeable
  //this is 4 different ways to do the same thing
  test("multiply the target value by 2 if it exists") {
    val results: Seq[Seq[Any]] = functions.map { case (name, f) => tryOutFunction(name, f) }.toSeq
    assert(results.size === 4)
    assert(results(0) === Seq("patternMatched", None, None, None, None, Some(2468)))
    assert(results(1) === Seq("stepByStep", None, None, None, None, Some(2468)))
    assert(results(2) === Seq("chained", None, None, None, None, Some(2468)))
    assert(results(3) === Seq("monadic", None, None, None, None, Some(2468)))
  }

  case class Baz(maybeValue: Option[Int])

  case class Bar(maybeBaz: Option[Baz])

  case class Foo(maybeBar: Option[Bar])

  val maybeFoo1: Option[Foo] = None
  val maybeFoo2: Option[Foo] = Some(Foo(None))
  val maybeFoo3: Option[Foo] = Some(Foo(Some(Bar(None))))
  val maybeFoo4: Option[Foo] = Some(Foo(Some(Bar(Some(Baz(None))))))
  val maybeFoo5: Option[Foo] = Some(Foo(Some(Bar(Some(Baz(Some(1234)))))))

  val maybeFooSequence = Seq(maybeFoo1, maybeFoo2, maybeFoo3, maybeFoo4, maybeFoo5)

  def patternMatched(maybeFoo: Option[Foo]): Option[Int] = {
    maybeFoo match {
      case Some(foo) => foo.maybeBar match {
        case Some(bar) => bar.maybeBaz match {
          case Some(baz) => baz.maybeValue match {
            case Some(value) => Some(value * 2)
            case None => None
          }
          case None => None
        }
        case None => None
      }
      case None => None
    }
  }

  def stepByStep(maybeFoo: Option[Foo]): Option[Int] = {
    val maybeBar: Option[Bar] = maybeFoo.flatMap(foo => foo.maybeBar)
    val maybeBaz: Option[Baz] = maybeBar.flatMap(bar => bar.maybeBaz)
    val maybeValue: Option[Int] = maybeBaz.flatMap(baz => baz.maybeValue)
    val maybeMultipliedBy2 = maybeValue.map(x => x * 2)
    maybeMultipliedBy2
  }

  def chained(maybeFoo: Option[Foo]): Option[Int] = {
    maybeFoo.flatMap(foo => foo.maybeBar).flatMap(bar => bar.maybeBaz).flatMap(baz => baz.maybeValue).map(x => x * 2)
  }

  def monadic(maybeFoo: Option[Foo]): Option[Int] = {
    for {
      foo <- maybeFoo
      bar <- foo.maybeBar
      baz <- bar.maybeBaz
      x <- baz.maybeValue
    } yield x * 2
  }

  val patternMatchedFunction = patternMatched _
  val stepByStepFunction = stepByStep _
  val chainedFunction = chained _
  val monadicFunction = monadic _

  val functions = Map(
    "patternMatched" -> patternMatchedFunction,
    "stepByStep" -> stepByStepFunction,
    "chained" -> chainedFunction,
    "monadic" -> monadicFunction
  )

  def tryOutFunction(heading: String, function: Option[Foo] => Option[Int]): Seq[Any] = {
    val results = maybeFooSequence.map(function)
    heading +: results
  }
}
