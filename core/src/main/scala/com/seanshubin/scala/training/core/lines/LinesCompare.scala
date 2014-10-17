package com.seanshubin.scala.training.core.lines

import scala.annotation.tailrec

sealed trait LinesCompareResult {
  def areSame: Boolean

  def toMultipleLineString: Seq[String]
}

object LinesCompareResult {

  case object Same extends LinesCompareResult {
    def areSame = true

    def toMultipleLineString = Seq()
  }

  case class MissingLine(lineNumber: Int, actualLines: Seq[String], expectedLines: Seq[String]) extends LinesCompareResult {
    def areSame = false

    def toMultipleLineString = {
      val line = expectedLines(lineNumber)
      val header = s"missing line at line $lineNumber: $line"
      Seq(header)
    }
  }

  case class ExtraLine(lineNumber: Int, actualLines: Seq[String], expectedLines: Seq[String]) extends LinesCompareResult {
    def areSame = false

    def toMultipleLineString = {
      val line = actualLines(lineNumber)
      val header = s"extra line at line $lineNumber: $line"
      Seq(header)
    }
  }

  case class Difference(lineNumber: Int, actualLines: Seq[String], expectedLines: Seq[String]) extends LinesCompareResult {
    def areSame = false

    def toMultipleLineString = {
      val actualLine = actualLines(lineNumber)
      val expectedLine = expectedLines(lineNumber)
      val header = s"difference at line $lineNumber"
      val actual = s"actual  : $actualLine"
      val expected = s"expected: $expectedLine"
      Seq(header, actual, expected)
    }
  }

}

object LinesCompare {
  def compareLines(actualSeq: Seq[String], expectedSeq: Seq[String]): LinesCompareResult = {
    import com.seanshubin.scala.training.core.lines.LinesCompareResult._

    @tailrec
    def compareLists(lineNumber: Int, remainingActual: List[String], remainingExpected: List[String]): LinesCompareResult = {
      (remainingActual.headOption, remainingExpected.headOption) match {
        case (Some(actualLine), Some(expectedLine)) =>
          if (actualLine == expectedLine) compareLists(lineNumber + 1, remainingActual.tail, remainingExpected.tail)
          else Difference(lineNumber, actualSeq, expectedSeq)
        case (Some(actualLine), None) => ExtraLine(lineNumber, actualSeq, expectedSeq)
        case (None, Some(expectedLine)) => MissingLine(lineNumber, actualSeq, expectedSeq)
        case (None, None) => Same
      }
    }
    compareLists(1, actualSeq.toList, expectedSeq.toList)
  }
}
