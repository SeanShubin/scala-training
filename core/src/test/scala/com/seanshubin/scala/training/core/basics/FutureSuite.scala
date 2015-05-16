package com.seanshubin.scala.training.core.basics

import java.util.concurrent.TimeUnit

import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

//Don't use Await except for samples or tests (this is a sample)
//Don't use an infinite duration except in samples
//The only reason I can think of to use await in production code is to cleanly shut down (don't use infinite duration)
class FutureSuite extends FunSuite {
  val maxTestTime = FiniteDuration(100, TimeUnit.MILLISECONDS)
  test("join two concurrent processes") {
    //start computing right away
    val futureTwo = Future[Int] {
      2
    }

    //start computing right away
    val futureThree = Future[Int] {
      3
    }

    //compose a new future from the others without waiting for any to be complete
    val futureResult = for {
      two <- futureTwo
      three <- futureThree
    } yield two * three

    //if you are testing, it is ok to wait until we are caught up
    Await.ready(futureResult, maxTestTime)

    //the future can be in three states: successful completion, failure, still running
    val resultDescription = futureResult.value match {
      case Some(Success(value)) => "success: " + value
      case Some(Failure(exception)) => "failure: " + exception.getMessage
      case None => "don't have an answer yet"
    }

    assert(resultDescription === "success: 6")
  }
}
