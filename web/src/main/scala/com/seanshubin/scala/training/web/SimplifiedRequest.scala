package com.seanshubin.scala.training.web

case class SimplifiedRequest(target: String, method: String, parameterMap: Map[String, Seq[String]]) {
  def toMultipleLineString: Seq[String] = Seq(
    "target = " + target,
    "method = " + method,
    "parameters:"
  ) ++ parameterMap.flatMap(parameterMapEntryToLines)

  private def parameterMapEntryToLines(parameterMapEntry: (String, Seq[String])): Seq[String] = {
    val (name, values) = parameterMapEntry
    for (value <- values) yield {
      s"  $name = $value"
    }
  }
}
