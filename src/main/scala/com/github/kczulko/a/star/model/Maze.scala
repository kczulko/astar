package com.github.kczulko.a.star.model

import scalaz.ListT
import scalaz.std.list._

case class Maze(colsCount: Int, cells: List[List[Cell]]) {

  private lazy val setOfCells = cells.flatten.toSet

  def findValidNeighboursOf(cell: Cell): Set[Cell] =
    (setOfCells - cell).filter {
      c => (c.colDistanceTo(cell) <= 1) &&
              (c.rowDistanceTo(cell) <= 1) &&
                  !c.isForbidden
    }

  def findStartingPoint: Either[String, Cell] =
    getSingleCellByPredicate(
      errWhenEmpty = "There is no starting point in maze definition.",
      errWhenMultiple = "More than one starting point found."
    )(_.isStartingPoint)

  def findGoalPoint: Either[String, Cell] =
    getSingleCellByPredicate(
      errWhenEmpty = "There is no goal point in maze definition.",
      errWhenMultiple = "More than one goal point found."
    )(_.isGoalPoint)

  private def getSingleCellByPredicate(errWhenEmpty: String, errWhenMultiple: String)
                                      (f: Cell => Boolean): Either[String, Cell] =
    cells.flatten.filter(f) match {
      case h :: Nil => Right(h)
      case _ :: _ => Left(errWhenMultiple)
      case _ => Left(errWhenEmpty)
    }

  override def toString: String =
    ListT(cells)
      .map(_.charIdentifier)
      .run
      .map(_.mkString(" "))
      .mkString("\n")
}
