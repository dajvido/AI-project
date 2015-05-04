package obj

import gui.BoardPanel

import java.io.File

/**
 * Created by dajvido on 04.05.15.
 */
class Board(val boardPanel : BoardPanel) {
  val board = Array.ofDim[Pos](Board.SIZE, Board.SIZE)
  var posUnderHero = new Pos(0, 0)

  for(xi <- 0 to Board.SIZE-1; yi <- 0 to Board.SIZE-1) {
    if (xi == 0 && yi == 0) board(0)(0) = new Pos(0, 0, img = Pos.HERO)
    else {
      val field = Pos.possibleField()
      board(xi)(yi) = new Pos(xi,
        yi,
        Pos.enterableFor(field),
        Pos.binFor(field),
        Pos.shopFor(field),
        Pos.imageFor(field))
    }
  }

  def canMove(side: String, x: Int, y: Int): Boolean = {
    if ((side == "NORTH" && y == 0) || (side == "SOUTH" && y == Board.SIZE-1) || (side == "WEST" && x == 0) || (side == "EAST" && x == Board.SIZE-1)) {
      return false
    } else {
      side match {
        case "NORTH" => board(x)(y - 1).canEnter
        case "WEST" => board(x - 1)(y).canEnter
        case "SOUTH" => board(x)(y + 1).canEnter
        case _ => board(x + 1)(y).canEnter
      }
    }
  }

  def whatUnder(img: File): File = {
    img match {
      case Pos.SHOP_GLASS => Pos.HERO_SHOP_GLASS
      case Pos.SHOP_METAL => Pos.HERO_SHOP_METAL
      case Pos.SHOP_PAPER => Pos.HERO_SHOP_PAPER
      case Pos.BIN_LOW => Pos.HERO_BIN_LOW
      case Pos.BIN_MEDIUM => Pos.HERO_BIN_MEDIUM
      case Pos.BIN_LARGE => Pos.HERO_BIN_LARGE
      case _ => Pos.HERO
    }
  }

  def setPos(x : Int, y : Int): Unit = {
    posUnderHero = board(x)(y)
    Pos.ofHero = Array(x, y)
    if (posUnderHero.isShop) {
      println("SHOP!")
      board(x)(y) = new Pos(x, y, img = whatUnder(posUnderHero.img))
    } else if (posUnderHero.isBin) {
      println("BIN!")
      board(x)(y) = new Pos(x, y, img = whatUnder(posUnderHero.img))
    } else {
      board(x)(y) = new Pos(x, y, img = Pos.HERO)
    }
  }

  def move(side: String): Unit = {
    val heroPosition = Pos.ofHero
    val x = heroPosition(0)
    val y = heroPosition(1)
    if (canMove(side, x, y)) {
      board(x)(y) = posUnderHero
      side match {
        case "NORTH" => {
          setPos(x, y - 1)
        }
        case "WEST" => {
          setPos(x - 1, y)
        }
        case "SOUTH" => {
          setPos(x, y + 1)
        }
        case _ => {
          setPos(x + 1, y)
        }
      }
      boardPanel.repaint()
//      if (posUnderHero.isShop) {
//        println("SHOP!")
//      } else if (posUnderHero.isBin) {
//        println("BIN!")
//      }
    }
  }
}

object Board {
  val SIZE = 20
}
