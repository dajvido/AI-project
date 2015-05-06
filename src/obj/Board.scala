package obj

import gui.BoardPanel
import obj.Direction._


class Board(val boardPanel: BoardPanel) {
  val board = Array.ofDim[Pos](Board.SIZE, Board.SIZE)
  var posUnderHero = new Pos(0, 0)
  var field = 0

  for (xi <- 0 to Board.SIZE - 1; yi <- 0 to Board.SIZE - 1) {
    field = Pos.mapField(xi, yi)
    board(xi)(yi) = new Pos(
      xi,
      yi,
      Pos.enterableFor(field),
      Pos.binFor(field),
      Pos.shopFor(field),
      Pos.imageFor(field)
    )
  }

  def isNotEdgeOfTheMap(direction: Direction, x: Int, y: Int): Boolean = {
    direction match {
      case NORTH => y != 0
      case SOUTH => y != Board.SIZE - 1
      case WEST => x != 0
      case _ => x != Board.SIZE - 1
    }
  }

  def isEnterable(direction: Direction, x: Int, y: Int): Boolean = {
    direction match {
      case NORTH => board(x)(y - 1).canEnter
      case WEST => board(x - 1)(y).canEnter
      case SOUTH => board(x)(y + 1).canEnter
      case _ => board(x + 1)(y).canEnter
    }
  }

  def canMove(direction: Direction, x: Int, y: Int): Boolean = {
    isNotEdgeOfTheMap(direction, x, y) && isEnterable(direction, x, y)
  }

  def setPos(x: Int, y: Int): Unit = {
    posUnderHero = board(x)(y)
    Pos.ofHero = Array(x, y)
    board(x)(y) = new Pos(x, y, img = Pos.imgUnderHero(posUnderHero.img))
  }

  def newPosition(direction: Direction, x: Int, y: Int): Unit = {
    direction match {
      case NORTH => setPos(x, y - 1)
      case WEST => setPos(x - 1, y)
      case SOUTH => setPos(x, y + 1)
      case _ => setPos(x + 1, y)
    }
  }

  def move(direction: Direction): Unit = {
    val heroPosition = Pos.ofHero
    val x = heroPosition(0)
    val y = heroPosition(1)
    if (canMove(direction, x, y)) {
      board(x)(y) = posUnderHero
      newPosition(direction, x, y)
      boardPanel.repaint()
    }
  }
}

object Board {
  val SIZE = 15
}
