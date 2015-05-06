package obj

import gui.BoardPanel


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

  def isNotEdgeOfTheMap(side: String, x: Int, y: Int): Boolean = {
    side match {
      case Values.TOWARDS_NORTH => y != 0
      case Values.TOWARDS_SOUTH => y != Board.SIZE - 1
      case Values.TOWARDS_WEST => x != 0
      case _ => x != Board.SIZE - 1
    }
  }

  def isEnterable(side: String, x: Int, y: Int): Boolean = {
    side match {
      case Values.TOWARDS_NORTH => board(x)(y - 1).canEnter
      case Values.TOWARDS_WEST => board(x - 1)(y).canEnter
      case Values.TOWARDS_SOUTH => board(x)(y + 1).canEnter
      case _ => board(x + 1)(y).canEnter
    }
  }

  def canMove(side: String, x: Int, y: Int): Boolean = {
    return isNotEdgeOfTheMap(side, x, y) && isEnterable(side, x, y)
  }

  def setPos(x: Int, y: Int): Unit = {
    posUnderHero = board(x)(y)
    Pos.ofHero = Array(x, y)
    board(x)(y) = new Pos(x, y, img = Pos.imgUnderHero(posUnderHero.img))
  }

  def newPosition(side: String, x: Int, y: Int): Unit = {
    side match {
      case Values.TOWARDS_NORTH => setPos(x, y - 1)
      case Values.TOWARDS_WEST => setPos(x - 1, y)
      case Values.TOWARDS_SOUTH => setPos(x, y + 1)
      case _ => setPos(x + 1, y)
    }
  }

  def move(side: String): Unit = {
    val heroPosition = Pos.ofHero
    val x = heroPosition(0)
    val y = heroPosition(1)
    if (canMove(side, x, y)) {
      board(x)(y) = posUnderHero
      newPosition(side, x, y)
      boardPanel.repaint()
    }
  }
}

object Board {
  val SIZE = 15
}
