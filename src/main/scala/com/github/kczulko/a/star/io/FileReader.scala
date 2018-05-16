package com.github.kczulko.a.star.io

import scala.util.Try

object FileReader {

  def getFilepath(args: Array[String]): Either[String, String] = {
    args.toList.headOption.fold[Either[String, String]](
      Left("Missing input argument.")
    )(
      Right(_)
    )
  }

  def readLines(filepath: String): Either[Throwable, List[String]] = {
    Try(scala.io.Source.fromFile(filepath))
      .toEither
      .map { bs =>
        val lines = bs.getLines().toList
        bs.close()
        lines
      }
  }

}
