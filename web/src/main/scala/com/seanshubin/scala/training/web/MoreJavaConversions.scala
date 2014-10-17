package com.seanshubin.scala.training.web

import java.util.Map.{Entry => JavaMapEntry}
import java.util.{Map => JavaMap}

import scala.collection.JavaConversions

object MoreJavaConversions {
  def toScalaMap(javaMap: JavaMap[String, Array[String]]): Map[String, Seq[String]] = {
    def toTuple2(javaEntry: JavaMapEntry[String, Array[String]]): (String, Seq[String]) = (javaEntry.getKey, javaEntry.getValue)
    JavaConversions.asScalaSet(javaMap.entrySet()).map(toTuple2).toMap
  }
}
