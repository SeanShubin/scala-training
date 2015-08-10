package com.seanshubin.scala.training.core.basics

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

//scala does not have enumerated types built in as a language construct
//you can extend the Enumeration type if you don't need inheritance
//you can have many case classes or case objects extend the same trait if you don't ever need to iterate over the values
//if you need both, here is a pattern I have found useful
sealed abstract case class Shape(name: String) {
  Shape.valuesBuffer += this

  def nameMatches(nameForComparison: String): Boolean = name.equalsIgnoreCase(nameForComparison)
}

object Shape {
  private val valuesBuffer = new ArrayBuffer[Shape]

  //either make values lazy, or move them to the bottom of the object
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
