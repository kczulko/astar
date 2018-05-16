package com.github.kczulko.a.star.model

import scala.math.{abs, sqrt}

case class Cell (ri: Int, ci: Int, charIdentifier: Char) {

  def isForbidden: Boolean = charIdentifier equals 'x'
  def isStartingPoint: Boolean = charIdentifier equals 's'
  def isGoalPoint: Boolean = charIdentifier equals 'g'

  def rowDistanceTo(other: Cell): Int = abs(ri - other.ri)
  def colDistanceTo(other: Cell): Int = abs(ci - other.ci)

  def distanceTo(other: Cell): Double = {
    def square: Double => Double = math.pow(_, 2)
    sqrt(square(rowDistanceTo(other)) + square(colDistanceTo(other)))
  }
}
