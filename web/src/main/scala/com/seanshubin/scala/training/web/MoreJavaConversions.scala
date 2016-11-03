package com.seanshubin.scala.training.web

import java.util.Map.{Entry => JavaMapEntry}
import java.util.{Map => JavaMap}

import scala.collection.JavaConverters._

object MoreJavaConversions {
  def toScalaMap(javaMap: JavaMap[String, Array[String]]): Map[String, Seq[String]] = {
    def toTuple2(javaEntry: JavaMapEntry[String, Array[String]]): (String, Seq[String]) = (javaEntry.getKey, javaEntry.getValue)
    javaMap.entrySet().asScala.map(toTuple2).toMap
  }
}
