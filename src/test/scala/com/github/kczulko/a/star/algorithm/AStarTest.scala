package com.github.kczulko.a.star.algorithm

import com.github.kczulko.a.star.algorithm.AStar.{resolveAgainst, retrievePath}
import com.github.kczulko.a.star.io.Parser
import com.github.kczulko.a.star.model.Cell
import org.scalatest.{EitherValues, FlatSpec, Matchers}

class AStarTest extends FlatSpec with Matchers with EitherValues {

  def positiveAStartCaseFor(input: List[String], expectedPath: Cell*): Unit = {
    it should s"pass the positive scenario for given input: \n${input.mkString("\n")}" in {
      val retrievedPath = for {
        finalState <- resolveAgainst(Parser.parse(input).right.value)
        path <- AStar.retrievePath(finalState)
      } yield path

      retrievedPath.right.value shouldEqual expectedPath
    }
  }

  def negativeAStartCaseFor(input: List[String], expectedErrorMsg: String): Unit = {
    it should s"fail the positive scenario for given input: \n${input.mkString("\n")}" in {
      resolveAgainst(Parser.parse(input).right.value).left.value should startWith(expectedErrorMsg)
    }
  }

  it should behave like positiveAStartCaseFor(
    input = """
              |2
              |2
              |sg
              |__
            """.stripMargin.trim.lines.toList,
    expectedPath = Cell(0, 0, 's'), Cell(0, 1, 'g')
  )

  it should behave like positiveAStartCaseFor(
    input = """
              |4
              |4
              |___s
              |x__x
              |_xxx
              |___g
            """.stripMargin.trim.lines.toList,
    expectedPath = Cell(0,3,'s'), Cell(1,2,'_'), Cell(1,1,'_'),
                   Cell(2,0,'_'), Cell(3,1,'_'), Cell(3,2,'_'), Cell(3,3,'g')
  )

  it should behave like negativeAStartCaseFor(
    input =
      """
        |2
        |2
        |_g
        |__
      """.stripMargin.trim.lines.toList,
    expectedErrorMsg = "There is no starting point in maze definition."
  )

  it should behave like negativeAStartCaseFor(
    input =
      """
        |2
        |2
        |_s
        |__
      """.stripMargin.trim.lines.toList,
    expectedErrorMsg = "There is no goal point in maze definition."
  )

  it should behave like negativeAStartCaseFor(
    input = """
              |4
              |4
              |___s
              |___x
              |xxxx
              |___g
            """.stripMargin.trim.lines.toList,
    expectedErrorMsg = "There is no solution for the given maze."
  )

}
