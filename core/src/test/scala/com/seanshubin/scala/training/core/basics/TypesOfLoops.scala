package com.seanshubin.scala.training.core.basics

import org.scalatest.FunSuite

import scala.annotation.tailrec

//the sample here associates colors with aliases
class TypesOfLoops extends FunSuite {
  //the input uses an outline format, with colors at the root level and aliases at the first indented level
  val colorAliasLines = Seq(
    "red",
    "    scarlet",
    "    ruby",
    "    flame",
    "green",
    "    jade",
    "    forest",
    "    mint",
    "blue",
    "    turquoise",
    "    azure",
    "    sapphire"
  )

  //we want to end up with a map from a color to a sequence of its aliases
  val expectedColorAliases = Map(
    "red" -> Seq("scarlet", "ruby", "flame"),
    "green" -> Seq("jade", "forest", "mint"),
    "blue" -> Seq("turquoise", "azure", "sapphire")
  )

  //define the patterns for a color and alias using regex.
  //the triple quotes tell the string not to expand escape sequences, which is very convenient for regular expressions
  //the .r method converts a string to a regular expression

  //if there are no spaces in front of the name, we treat it as a color
  val ColorPattern = """(\w+)""".r

  //if there are spaces in front of the name, we treat it as a name
  //notice that we don't capture the spaces, so our regular expression extractor will only get the name
  val AliasPattern = """\s+(\w+)""".r

  test("loop, mutable") {
    //we can import anywhere we can put statements, which is useful if we only want to import something to a limited scope
    //we can also specify aliases, here MutableMap is an alias for collection.mutable.Map
    import scala.collection.mutable.{ArrayBuffer, Map => MutableMap}

    //create the mutable storage area for our aliases
    val actualColorAliases: MutableMap[String, ArrayBuffer[String]] = MutableMap()

    //keep track of which color we are currently adding aliases to
    var currentColor = "<none>"

    //this method returns nothing, so we know it must be side effecting
    def updateWithLine(line: String) {
      line match {
        case ColorPattern(colorName) =>
          currentColor = colorName
          actualColorAliases(currentColor) = ArrayBuffer[String]()
        case AliasPattern(aliasName) => actualColorAliases(currentColor) += aliasName
      }
    }

    //foreach applies a method to each element
    //since foreach does not return a new list, we know it must be side effecting
    colorAliasLines.foreach(updateWithLine)

    //equality for Scala's collections looks at the values only
    //it does not consider different collection implementations to be different if they have the same values
    //which is why we can compare a mutable.Map to a Map, and a Seq to an ArrayBuffer
    assert(actualColorAliases === expectedColorAliases)
  }

  //here is how we do the same thing with a fold
  test("fold with builder") {
    //for a fold we have to place all of our state in a single place
    //our state is the current color, and the color aliases result so far
    //so we place both of these in the ColorAliasBuilder object
    //here we use a case class to indicate we want value object semantics
    //since it is a value object, we want it to be immutable
    //so instead of setters, we define transformations to convert an existing object to a new object with the change
    case class ColorAliasBuilder(currentColor: String, colorAliases: Map[String, Seq[String]]) {
      //define the initial state, when no lines have been processed
      def this() = this("<none>", Map().withDefaultValue(Seq()))

      //return a new instance with the currentColor updated
      //the copy method was generated by making this a 'case' class
      //we use named parameters to indicate what changed
      def setCurrentColor(newCurrentColor: String) = copy(currentColor = newCurrentColor)

      //create a new instance with an alias added for the current color
      def addAlias(aliasName: String) = {
        val newColorAliases = colorAliases.updated(currentColor, colorAliases(currentColor) :+ aliasName)
        copy(colorAliases = newColorAliases)
      }
    }

    //for a fold, we have to define the transition from the current accumulator and the next element, to the new accumulator
    //here our accumulator is the builder, all of the state goes here
    //our element is the line
    def updateBuilderWithLine(builder: ColorAliasBuilder, line: String): ColorAliasBuilder = {
      line match {
        case ColorPattern(colorName) => builder.setCurrentColor(colorName)
        case AliasPattern(aliasName) => builder.addAlias(aliasName)
      }
    }

    //here we do the fold
    //the first parameter list contains the initial value for our accumulator
    //the second parameter list contains the function that takes the old accumulator and current line and returns a new accumulator
    val builder = colorAliasLines.foldLeft(new ColorAliasBuilder())(updateBuilderWithLine)

    //we only grab the part of the accumulator we care about
    //the current color was necessary when building the aliases, but is not necessary once we have our list
    //so just get the color aliases map
    val actualColorAliases = builder.colorAliases

    assert(actualColorAliases === expectedColorAliases)
  }

  //here is how to do the same thing with tail recursion
  test("recursion") {
    //for tail recursion, all of our state must go in the parameter list
    //here, that is our color aliases so far, and the current color
    //also we have to pass along a way to get the remaining lines, here expressed as a list
    //the @tailrec annotation makes the compiler enforce that the recursion is in the tail position
    @tailrec
    def addRemainingLines(colorAliases: Map[String, Seq[String]], currentColor: String, remainingLines: List[String]): Map[String, Seq[String]] = {
      //if we are out of remaining lines, we know that what we have built so far is complete
      if (remainingLines.isEmpty) colorAliases
      else {
        //get the next line
        val line = remainingLines.head
        line match {
          //if the line is a color
          //leave the color aliases the same
          //use the new color name instead of the current color
          //don't send along the line we have just processed, done here with the .tail function
          case ColorPattern(colorName) => addRemainingLines(colorAliases, colorName, remainingLines.tail)

          //if the line is an alias, update the color aliases map
          //send along the new color aliases
          //leave current color the same
          //don't send along the line we have just processed, done here with the .tail function
          case AliasPattern(aliasName) =>
            val newColorAliases = colorAliases.updated(currentColor, colorAliases(currentColor) :+ aliasName)
            addRemainingLines(newColorAliases, currentColor, remainingLines.tail)
        }
      }
    }

    //define the initial value for our accumulator
    val emptyColorAliases = Map[String, Seq[String]]().withDefaultValue(Seq())

    //define the initial value for the current color
    val noColor = "<none>"

    //call the recursive function
    //go ahead and force lines to use a list implementation with the .toList function
    //a list is most efficient for cases where the only things you do to it are .head, .tail, and .isEmpty
    //this is commonly the case with recursive implementations
    val actualColorAliases = addRemainingLines(emptyColorAliases, noColor, colorAliasLines.toList)

    assert(actualColorAliases === expectedColorAliases)
  }
}
