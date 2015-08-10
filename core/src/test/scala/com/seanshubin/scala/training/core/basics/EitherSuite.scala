package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

class EitherSuite extends FunSuite {
  //a common use for Either is validation, putting a valid result on one alternative, an invalid result in the other
  //since "right" in a different context can mean "correct", it was observed that this can be used as a mnemonic
  //this has led to a convention of putting the valid answer on the right, and the invalid answer on the left
  //remember, the "right" side of the either contains the "right" (correct) answer
  test("stop on first validation error") {
    //lets validate a bunch of samples, ensuring we get the correct messages each time
    val sampleInputs = Seq(null, "", "aaa", "-1", "234", "50")
    val actual = sampleInputs.map(ValidationRules.validateQuality)
    val expected: Seq[Either[String, Int]] = Seq(
      Left("must not be null"),
      Left("must not be blank"),
      Left("must be a number, was 'aaa'"),
      Left("must be at least 1, was -1"),
      Left("must be at most 100, was 234"),
      Right(50)) //here the validation was successful, so we have the "right" answer, statically typed
    assert(actual === expected)
  }

  //An example of how you might validate if you needed a list of all the validation errors
  //The rules are as follows:
  //name
  //  must not be null
  //  must not contain whitespace
  //shape
  //  must be one of Triangle, Circle, Square
  //quality
  //  must not be null
  //  must not be blank
  //  must be a number
  //  must be at least 1
  //  must be at most 100
  test("accumulate validation errors") {
    //when everything is valid, we should get the statically typed value
    assert(
      PartValidator.validate(Map("name" -> "bit", "shape" -> "triangle", "quality" -> "79")) ===
        Right(Part("bit", Shape.Triangle, 79)))
    //if there are validation errors, we should get the list of validation messages
    assert(
      PartValidator.validate(Map("name" -> "bit and a bob", "shape" -> "trapezoid", "quality" -> "wat")) ===
        Left(Seq(
          "name must not contain whitespace, was 'bit and a bob'",
          "shape was 'trapezoid', expected one of Triangle, Circle, Square",
          "quality must be a number, was 'wat'")))
  }

  object ValidationRules {
    def disallowNull(input: String): Either[String, String] = {
      if (input == null) Left("must not be null")
      else Right(input)
    }

    def disallowBlank(input: String): Either[String, String] = {
      if (input.trim == "") Left("must not be blank")
      else Right(input)
    }

    def disallowWhitespace(input: String): Either[String, String] = {
      if (input.matches( """.*\s.*""")) Left(s"must not contain whitespace, was '$input'")
      else Right(input)
    }

    def requireNumber(input: String): Either[String, Int] = {
      try {
        Right(input.toInt)
      } catch {
        case _: NumberFormatException => Left(s"must be a number, was '$input'")
      }
    }

    //this validation rule has parameters
    //so rather than doing the validation
    //create a function that does the validation with the proper parameters
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

    //compose a bunch of rules in order, stop on the first failure
    def validateQuality(input: String): Either[String, Int] = {
      //if we only need to record the first failure, we can compose along the "right" projection
      //unlike options, it is not obvious if we want to follow the left path or right path
      //so we have to specify it in the for comprehension
      //note that if we project right on a left, nothing happens
      //if we project right on a right, the statically typed value is carried over
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

    def validateName(input: String): Either[String, String] = {
      for {
        a <- disallowNull(input).right
        b <- disallowWhitespace(a).right
      } yield {
        b
      }
    }
  }

  case class Part(name: String, shape: Shape, quality: Long)

  case class PartValidator(inputs: Map[String, String],
                           messages: List[String],
                           maybeName: Option[String],
                           maybeShape: Option[Shape],
                           maybeQuality: Option[Long]) {
    //compose the final validation result
    def toValidated: Either[Seq[String], Part] = {
      val validated = PartValidator.validatePart(this)
      val result =
        if (validated.isValid) Right(Part(validated.maybeName.get, validated.maybeShape.get, validated.maybeQuality.get))
        else Left(validated.messages.reverse)
      result
    }

    def isValid = messages.isEmpty
  }

  object PartValidator {

    import ValidationRules._

    def validate(map: Map[String, String]): Either[Seq[String], Part] =
      PartValidator(map, Nil, None, None, None).toValidated

    def fromMap(map: Map[String, String]) = PartValidator(map, Nil, None, None, None)

    //transform using the current validation rule
    def applyPartRule(soFar: PartValidator, rule: PartValidator => PartValidator): PartValidator = {
      val newValidator = rule(soFar)
      newValidator
    }

    //not using Either here, because we need to collect all the messages, so can't stop on hitting a "left"
    def validatePartName(partValidator: PartValidator): PartValidator = {
      validateName(partValidator.inputs.get("name").orNull) match {
        case Left(message) => partValidator.copy(messages = s"name $message" :: partValidator.messages)
        case Right(validName) => partValidator.copy(maybeName = Some(validName))
      }
    }

    def validatePartShape(partValidator: PartValidator): PartValidator = {
      Shape.eitherFromString(partValidator.inputs.get("shape").orNull) match {
        case Left(message) => partValidator.copy(messages = s"shape $message" :: partValidator.messages)
        case Right(validShape) => partValidator.copy(maybeShape = Some(validShape))
      }
    }

    def validatePartQuality(partValidator: PartValidator): PartValidator = {
      validateQuality(partValidator.inputs.get("quality").orNull) match {
        case Left(message) => partValidator.copy(messages = s"quality $message" :: partValidator.messages)
        case Right(validId) => partValidator.copy(maybeQuality = Some(validId))
      }
    }

    //run all the rules in order
    def validatePart(partValidator: PartValidator): PartValidator = {
      val partRules = Seq(
        validatePartName _,
        validatePartShape _,
        validatePartQuality _)
      val validated = partRules.foldLeft(partValidator)(applyPartRule)
      validated
    }
  }

}
