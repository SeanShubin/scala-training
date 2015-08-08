package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

class EitherSuite extends FunSuite {
  test("stop on first validation error") {
    val sampleInputs = Seq(null, "", "aaa", "-1", "234", "50")
    val actual = sampleInputs.map(ValidationRules.validatePartId)
    val expected: Seq[Either[String, Int] with Product with Serializable] = Seq(
      Left("must not be null"),
      Left("must not be blank"),
      Left("must be a number, was 'aaa'"),
      Left("must be at least 1, was -1"),
      Left("must be at most 100, was 234"),
      Right(50))
    assert(actual === expected)
  }

  //  test("accumulate validation errors") {
  //    val partUserInput = Map("id" -> "123", "name" -> "bit", "shape" -> "triangle")
  //    case class Part(id: Long, name: String, shape: Shape)
  //    case class PartValidator(inputs: Map[String, String], messages: Seq[String], maybeId: Option[Long], maybeName: Option[String], maybeShape: Option[Shape])
  //    def idMustConvertToLong(partValidator: PartValidator) = {
  //      validateId(partValidator.inputs)
  //    }
  //  }

  object ValidationRules {
    def disallowNull(input: String): Either[String, String] = {
      if (input == null) Left("must not be null")
      else Right(input)
    }

    def disallowBlank(input: String): Either[String, String] = {
      if (input.trim == "") Left("must not be blank")
      else Right(input)
    }

    def requireNumber(input: String): Either[String, Int] = {
      try {
        Right(input.toInt)
      } catch {
        case _: NumberFormatException => Left(s"must be a number, was '$input'")
      }
    }

    def requireAtLeast(atLeast: Int): Int => Either[String, Int] =
      (input: Int) => {
        if (input < atLeast) Left(s"must be at least $atLeast, was $input")
        else Right(input)
      }

    def requireAtMost(atMost: Int): Int => Either[String, Int] =
      (input: Int) => {
        if (input > atMost) Left(s"must be at most $atMost, was $input")
        else Right(input)
      }

    def requireAtLeast1 = requireAtLeast(1)

    def requireAtMost100 = requireAtMost(100)

    def validatePartId(input: String): Either[String, Int] = {
      for {
        a <- disallowNull(input).right
        b <- disallowBlank(a).right
        c <- requireNumber(b).right
        d <- requireAtLeast1(c).right
        e <- requireAtMost100(d).right
      } yield {
        e
      }
    }
  }

  sealed abstract case class Shape(name: String) {
    Shape.valuesBuffer += this

    def nameMatches(nameForComparison: String): Boolean = name.equalsIgnoreCase(nameForComparison)
  }

  object Shape {
    private val valuesBuffer = new ArrayBuffer[Shape]
    lazy val values = valuesBuffer.toSeq
    val Triangle = new Shape("Triangle") {}
    val Circle = new Shape("Circle") {}
    val Square = new Shape("Square") {}

    //when it makes sense to assume an error will not happen
    def fromString(name: String): Shape = {
      values.find(_.nameMatches(name)).head
    }

    //when the ways an error can happen are known, and no further information about the error is needed
    def maybeFromString(name: String): Option[Shape] = {
      values.find(_.nameMatches(name))
    }

    //when the ways an error can happen are known, and further information about the error is needed
    def eitherFromString(name: String): Either[String, Shape] = {
      values.find(_.nameMatches(name)) match {
        case Some(shape) => Right(shape)
        case None =>
          val validNamesString = values.map(_.name).mkString(", ")
          Left(s"Got '$name', but expected one of $validNamesString")
      }
    }

    //when the ways an error can happen are not known, but need to be handled by the caller
    def tryFromString(name: String): Try[Shape] = {
      Try.apply {
        values.find(shape => shape.nameMatches(name)).head
      }
    }
  }

}
