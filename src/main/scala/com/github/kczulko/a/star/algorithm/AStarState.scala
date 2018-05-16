package com.github.kczulko.a.star.algorithm

import com.github.kczulko.a.star.model.Cell
import scalaz.Scalaz._
import scalaz.State

import scala.collection.GenTraversableOnce
import scala.util.Try

case class AStarState(start: Cell, goal: Cell,
                                         open: Set[Cell], closed: Set[Cell],
                                         g: Map[Cell, Double], f: Map[Cell, Double],
                                         cameFrom: Map[Cell, Cell]) {

  def getOpenCellWithLowestCost: Option[Cell] =
    Try(open.minBy(f.getOrElse(_, Double.MaxValue))).toOption

}

object AStarState {

  def addToOpen(cells: GenTraversableOnce[Cell]): State[AStarState, Set[Cell]] = State(as => {
    val ns = as.copy(open = as.open ++ cells)
    (ns, ns.open)
  })

  def removeFromOpen(cell: Cell): State[AStarState, Set[Cell]] = State(as => {
    val ns = as.copy(open = as.open - cell)
    (ns, ns.open)
  })

  def addToClosed(cell: Cell): State[AStarState, Set[Cell]] = State(as => {
    val ns = as.copy(closed = as.closed + cell)
    (ns, ns.closed)
  })

  def filterPromisingOnlyNeighbours(neighbours: Set[Cell]): Cell => State[AStarState, Set[Cell]] =
    current =>
      State(as => {
        val currentGScore = as.g.getOrElse(current, Double.MaxValue)
        val promisingNeighbours = neighbours.filter { neighbour =>
          currentGScore + neighbour.distanceTo(current) < as.g.getOrElse(neighbour, Double.MaxValue)
        }
        (as, promisingNeighbours)
      })

  def updateMetricsAndPath(neighbours: Set[Cell]): Cell => State[AStarState, Unit] =
    current => {
      def updateMetricsAndPathForEachNeighbourOfCurrent(neighbour: Cell): State[AStarState, Unit] =
        State(as => {
          val neighbourGScore = as.g.getOrElse(current, Double.MaxValue) + current.distanceTo(neighbour)
          val neighbourHeuristic = neighbour.distanceTo(as.goal)
          val ns = as.copy(
            g = as.g + (neighbour -> neighbourGScore),
            f = as.f + (neighbour -> (neighbourGScore + neighbourHeuristic)),
            cameFrom = as.cameFrom + (neighbour -> current)
          )
          (ns, ())
        })

      neighbours.toList
        .traverseS(updateMetricsAndPathForEachNeighbourOfCurrent)
        .map(_ => ())
    }
}