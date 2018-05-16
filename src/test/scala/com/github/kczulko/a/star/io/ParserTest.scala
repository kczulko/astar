package com.github.kczulko.a.star.io

import com.github.kczulko.a.star.model.{Cell, Maze}
import org.scalatest.{EitherValues, FlatSpec, Matchers}
import com.github.kczulko.a.star.io.Parser.parse

class ParserTest extends FlatSpec with Matchers with EitherValues {

  it should "parse 2x2 maze" in {
    val input =
      """
        |2
        |2
        |s _
        |_ g
      """.stripMargin.trim.lines.toList

    parse(input).right.value shouldEqual
      Maze(2,
        List(
          List(Cell(0,0,'s'), Cell(0,1,'_')),
          List(Cell(1,0,'_'), Cell(1,1,'g'))
        )
      )
  }

  it should "fail when number of declared rows is different" in {
    val input = """
      |2
      |2
      |s _
      |_ g
      |_ _
    """.stripMargin.trim.lines.toList

    parse(input).left.value shouldEqual
      "Invalid number of rows while parsing maze. Expected 2, but was 3"
  }

  it should "fail when at least for one row has different number columns than it was declared" in {
    val input = """
                  |2
                  |2
                  |s _
                  |_ g _
                """.stripMargin.trim.lines.toList

    parse(input).left.value shouldEqual
      "Invalid number of columns at row[2] while parsing maze."
  }

  it should "fail when declared rows is not a number" in {
    val input = """
                  |asdfqwerty
                  |2
                  |s _
                  |_ g _
                """.stripMargin.trim.lines.toList

    parse(input).left.value should startWith ("Cannot parse line with rows number.")
  }

  it should "fail when declared cols is not a number" in {
    val input = """
                  |2
                  |asdfqwerty
                  |s _
                  |_ g _
                """.stripMargin.trim.lines.toList

    parse(input).left.value should startWith ("Cannot parse line with cols number.")
  }

  it should "fail when provided input is empty" in {
    parse(List.empty).left.value should startWith ("Provided input doesn't follow expected schema.")
  }

  it should "fail when unexpected character is a member of maze" in {
    val input = """
                  |2
                  |2
                  |s @
                  |_ g
                """.stripMargin.trim.lines.toList
    
    parse(input).left.value should startWith (
      "Unexpected character ['@'] occurred while parsing maze at (row[1],col[2])"
    )
  }
}
