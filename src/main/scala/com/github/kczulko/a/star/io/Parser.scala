package com.github.kczulko.a.star.io

import com.github.kczulko.a.star.model.{Cell, Maze}
import scalaz.Scalaz._
import scalaz._

import scala.util.Try

object Parser {

  def parse(lines: List[String]): Either[String, Maze] =
    lines match {
      case rowsLine :: colsLine :: mazeLines =>
        for {
          cols <- parseTo[Int](Integer.parseInt(colsLine.trim), "Cannot parse line with cols number.")
          rows <- parseTo[Int](Integer.parseInt(rowsLine.trim), "Cannot parse line with rows number.")
          maze <- parseMaze(rows, cols, mazeLines)
        } yield maze
      case _ => Left("Provided input doesn't follow expected schema.")
    }

  private def parseTo[A](a: => A, errorMsg: String): Either[String, A] = {
    Try(a).toEither.leftMap { throwable =>
      s"$errorMsg\n${throwable.getMessage}"
    }
  }

  private def parseMaze(rows: Int, cols: Int, mazeRows: List[String]): Either[String, Maze] = {
    val cellsE = mazeRows.map(_.replace(" ", "")).zipWithIndex.map {
      case (row, ri) =>
        val cellsInRow = row.zipWithIndex.toList.map {
          case r @ ('_' | 's' | 'g' | 'x', ci) =>  \/-(Cell(ri, ci, r._1))
          case (other, ci) =>
            -\/(s"Unexpected character ['$other'] occurred while parsing maze at (row[${ri + 1}],col[${ci + 1}])")
        }

        if (cellsInRow.size != cols)
          -\/(s"Invalid number of columns at row[${ri + 1}] while parsing maze.")
        else
          cellsInRow.sequenceU
    }.sequenceU

    for {
      cells <- cellsE.toEither
      _     <- Either.cond(
                 cells.size == rows,
                 cells,
                 s"Invalid number of rows while parsing maze. Expected $rows, but was ${cells.size}"
               )
    } yield Maze(cols, cells)
  }
}
