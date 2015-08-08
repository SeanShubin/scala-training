package com.seanshubin.scala.training.core.basics

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

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
        Left(s"was '$name', but expected one of $validNamesString")
    }
  }

  //when the ways an error can happen are not known, but need to be handled by the caller
  def tryFromString(name: String): Try[Shape] = {
    Try.apply {
      values.find(shape => shape.nameMatches(name)).head
    }
  }
}
