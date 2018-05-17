package com.github.kczulko.a.star.algorithm

import com.github.kczulko.a.star.algorithm.AStarState._
import com.github.kczulko.a.star.model.{Cell, Maze}

import scala.annotation.tailrec

object AStar {

  def resolveAgainst(maze: Maze): Either[String, AStarState] = {

    @tailrec
    def loop(actualState: AStarState): Either[String, AStarState] =
      actualState.getOpenCellWithLowestCost match {
        case None => Left("There is no solution for the given maze.")
        case Some(last) if last == actualState.goal => Right(actualState)
        case Some(current) =>
          val receipt = for {
            open       <- removeFromOpen(current)
            closed     <- addToClosed(current)
            neighbours =  maze.findValidNeighboursOf(current) diff closed diff open
            _          <- addToOpen(neighbours)
            promising  <- filterPromisingNeighbours(neighbours)(current)
            _          <- updateMetricsAndPath(promising)(current)
          } yield ()

          val (nextState, _) = receipt.run(actualState)
          loop(nextState)
      }

    for {
      start <- maze.findStartingPoint
      goal  <- maze.findGoalPoint
      initialState =
        AStarState(
          start = start,
          goal = goal,
          open = Set(start),
          closed = Set.empty,
          gScores = Map(start -> 0),
          fScores = Map.empty,
          cameFrom = Map.empty
        )
      finalState  <- loop(initialState)
    } yield finalState
  }

  def retrievePath(as: AStarState): Either[String, List[Cell]] = {
    import as._

    @tailrec
    def loop(child: Cell, path: List[Cell]): Either[String, List[Cell]] =
      cameFrom.get(child) match {
        case Some(cell) => loop(cell, cell :: path)
        case None if child == start => Right(path)
        case _ => Left(s"Cannot retrieve path from given state: $as")
      }

    loop(goal, List(goal))
  }
}
