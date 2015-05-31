package obj

import gui.BoardPanel
import obj.Direction._
import obj.astar.AStar

import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer


object Board {
  val panel = new BoardPanel
  val SIZE = 15
  val board = Array.ofDim[Pos](SIZE, SIZE)
  val boardList = List[Pos]()
  var posUnderHero = new Pos(0, 0)
  var field = 0

  for (xi <- 0 to SIZE - 1; yi <- 0 to SIZE - 1) {
    field = Pos.mapField(xi, yi)
    board(xi)(yi) = new Pos(
      x = xi,
      y = yi,
      g = Pos.moveCost(field),
      canEnter = Pos.enterableFor(field),
      isBin = Pos.binFor(field),
      isShop = Pos.shopFor(field),
      img = Pos.imageFor(field)
    )
    board(xi)(yi) :: boardList
  }

  def isNotEdgeOfTheMap(direction: Direction, x: Int, y: Int): Boolean = {
    direction match {
      case NORTH => y != 0
      case SOUTH => y != SIZE - 1
      case WEST => x != 0
      case _ => x != SIZE - 1
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
    Pos.ofHero = (x, y)
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
    val x = heroPosition._1
    val y = heroPosition._2
    if (canMove(direction, x, y)) {
      board(x)(y) = posUnderHero
      newPosition(direction, x, y)
      println("x, y = " + x + ", " + y)
      panel.repaint()
    }
  }

  def moveList(movementInstruction: ListBuffer[Direction]): Unit = {
    new Thread {
      override def run(): Unit = {
        movementInstruction.foreach(nextDirection => {
          move(nextDirection)
          Thread.sleep(200)
        })
      }
    }.start()
  }

  def doActions(actions: ListBuffer[() => Unit]): Unit = {
    new Thread {
      override def run(): Unit = {
        actions.foreach(action => {
          action()
          Thread.sleep(200)
        })
      }
    }.start()
  }

  def moveTo(targetPos: (Int, Int)): Unit = {
    val astar = new AStar(Pos.ofHero, targetPos)
    astar.generatePathMap()
    val actions = astar.getPath
    Board.doActions(actions)
  }
}