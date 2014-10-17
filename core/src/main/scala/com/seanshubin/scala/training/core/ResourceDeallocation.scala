package com.seanshubin.scala.training.core

object ResourceDeallocation {
  type HasClose = {
    def close()
  }

  def ensureClose[BlockResultType, ClosableType <: HasClose](closeMe: ClosableType)(codeBlock: ClosableType => BlockResultType) = {
    try {
      codeBlock(closeMe)
    } finally {
      import scala.language.reflectiveCalls
      closeMe.close()
    }
  }
}
