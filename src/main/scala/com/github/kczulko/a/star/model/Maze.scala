package com.github.kczulko.a.star.model

case class Maze(colsCount: Int, cells: List[List[Cell]]) {

  def findNeighboursFor(cell: Cell): Set[Cell] = {
    (cells.flatten.toSet - cell).filter {
      c => (c.colDistanceTo(cell) <= 1) && (c.rowDistanceTo(cell) <= 1)
    }
  }

  def findValidNeighboursOf(cell: Cell): Set[Cell] =
    (cells.flatten.toSet - cell).filter {
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

  override def toString: String = {
    cells.map(_.map(_.charIdentifier).mkString(" ")).mkString("\n")
  }
}
