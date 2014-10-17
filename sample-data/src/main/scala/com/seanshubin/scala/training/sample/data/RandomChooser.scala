package com.seanshubin.scala.training.sample.data

trait RandomChooser {
  def fromSeq[T](seq: Seq[T]): T
}
