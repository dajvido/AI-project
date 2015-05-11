package obj

import scala.collection.mutable.ListBuffer


class Bin(override val x: Int, override val y: Int, var content: ListBuffer[Trash]) extends Pos(x, y) {
  var weight = content.length

  def isFull: Boolean = weight == 5

  def isAlmostFull: Boolean = weight >= 3

  def isEmpty: Boolean = weight == 0
}