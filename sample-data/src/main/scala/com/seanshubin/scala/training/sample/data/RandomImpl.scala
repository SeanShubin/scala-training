package com.seanshubin.scala.training.sample.data

import scala.util.Random

class RandomImpl(random: Random) extends RandomIfc {
  def nextInt(n: Int): Int = random.nextInt(n)
}
