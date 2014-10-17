package com.seanshubin.scala.training.sample.data

class RandomChooserImpl(random: RandomIfc) extends RandomChooser {
  def fromSeq[T](seq: Seq[T]): T = {
    val index = random.nextInt(seq.size)
    seq(index)
  }
}
