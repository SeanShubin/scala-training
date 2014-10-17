package com.seanshubin.scala.training.core

sealed trait Query

case class NameQuery(name: String) extends Query

case class ColorAndNameQuery(color: String, name: String) extends Query
