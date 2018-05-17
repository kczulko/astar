package com.github.kczulko.a.star

import com.github.kczulko.a.star.algorithm.{AStar, PrintableResult}
import com.github.kczulko.a.star.io.{FileReader, Parser}

object Main {

  def main(args: Array[String]): Unit = {

    lazy val result = for {
      filepath <- FileReader.getFilepath(args)
      lines <- FileReader.readLines(filepath)
      maze <- Parser.parse(lines)
      finalState <- AStar.resolveAgainst(maze)
      path <- AStar.retrievePath(finalState)
      expandedLocationsAmount = finalState.cameFrom.size
    } yield PrintableResult(maze, path, expandedLocationsAmount)

    println(
      result.fold(identity, identity)
    )
  }
}
