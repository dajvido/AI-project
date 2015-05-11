package obj

import scala.collection.mutable.ListBuffer


class Bin(override val x: Int, override val y: Int, val content: ListBuffer[Trash]) extends Pos(x, y) {
  val weight = content.length
}