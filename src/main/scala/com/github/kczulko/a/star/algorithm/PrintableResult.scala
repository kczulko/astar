package com.github.kczulko.a.star.algorithm

import com.github.kczulko.a.star.model.{Cell, Maze}

case class PrintableResult(maze: Maze, path: List[Cell], expandedLocationsAmount: Int) {

  def mazeWithPath: Maze = {
    val pathNodes = path.toSet

    maze.copy(
      cells = for {
        row <- maze.cells
      } yield row.map { cell =>
        if (pathNodes.contains(cell) && !cell.isStartingPoint && !cell.isGoalPoint)
          cell.copy(charIdentifier = '*')
        else
          cell
      }
    )
  }

  override def toString: String = {

    val separator = "==" * maze.colsCount

    s"""
      |Input maze:
      |
      |$maze

      |$separator
      |
      |Maze with path from 's' to 'g' (indicated by '*' character):
      |
      |$mazeWithPath
      |
      |$separator
      |
      |Path found (rows and cols are indexed from 0):
      |
      |$path
      |
      |$separator
      |
      |Expanded locations number: $expandedLocationsAmount
      |
    """.stripMargin
  }
}
