package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

class RegexExtractorSuite extends FunSuite {
  //you convert a string to a regular expression by invoking the .r method
  //triple quoted strings do not evaluate escape sequences, which is convenient when you are defining regular expressions
  val SimplePhoneNumberPattern = """(\d+)-(\d+)""".r
  val AreaCodeAndPhoneNumberPattern = """\((\d+)\) (\d+)-(\d+)""".r

  test("simple phone number") {
    //if we are sure we have a match, we can invoke the extractor directly
    val SimplePhoneNumberPattern(firstPart, secondPart) = "123-4567"
    assert(firstPart === "123")
    assert(secondPart === "4567")
  }

  test("area code and phone number") {
    //if we are sure we have a match, we can invoke the extractor directly
    val AreaCodeAndPhoneNumberPattern(areaCode, firstPart, secondPart) = "(555) 123-4567"
    assert(areaCode === "555")
    assert(firstPart === "123")
    assert(secondPart === "4567")
  }

  test("not sure which type of phone number") {
    //define a holder for what we extract from a regular expression
    case class PhoneNumber(maybeAreaCode: Option[String], firstPart: String, secondPart: String)

    def parsePhoneNumber(target: String): Option[PhoneNumber] = target match {
      case AreaCodeAndPhoneNumberPattern(areaCode, firstPart, secondPart) => Some(PhoneNumber(Some(areaCode), firstPart, secondPart))
      case SimplePhoneNumberPattern(firstPart, secondPart) => Some(PhoneNumber(None, firstPart, secondPart))
      case _ => None
    }

    val withAreaCode = "(555) 123-4567"
    val simple = "123-4567"
    val invalid = "blah"

    assert(parsePhoneNumber(withAreaCode) === Some(PhoneNumber(Some("555"), "123", "4567")))
    assert(parsePhoneNumber(simple) === Some(PhoneNumber(None, "123", "4567")))
    assert(parsePhoneNumber(invalid) === None)
  }
}
