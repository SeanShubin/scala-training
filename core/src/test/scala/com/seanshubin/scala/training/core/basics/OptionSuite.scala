package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

class OptionSuite extends FunSuite {

  case class User(name: String, password: String)

  //an option gives you a means to communicate that something could be null
  def searchForUserNamed(userName: String): Option[User] = {
    if (userName == "Alice")
      Some(User("Alice", "alicePassword"))
    else
      None
  }

  test("find user by name") {
    val actual = searchForUserNamed("Alice")
    assert(actual === Some(User("Alice", "alicePassword")))
  }

  test("search for user by name but don't find") {
    val actual = searchForUserNamed("Bob")
    assert(actual === None)
  }

  test("display search result") {
    def searchResult(userName: String) = searchForUserNamed(userName) match {
      case Some(user) => s"found user ${user.name} with password ${user.password}"
      case None => s"didn't find user $userName"
    }
    assert(searchResult("Alice") === "found user Alice with password alicePassword")
    assert(searchResult("Bob") === "didn't find user Bob")
  }
}
