package obj.astar

import java.io.FileWriter

import obj.Direction._
import obj.astar.exceptions.{NodeFoundedException, TargetNodeFoundedException}
import obj.{Board, Pos, Values}

import scala.collection.mutable.ListBuffer
import scala.math.{pow, sqrt}


class AStar(val startPosition: (Int, Int), val targetPosition: (Int, Int)) {

  var openNodes = new ListBuffer[Node]()
  var closedNodes = new ListBuffer[Node]()

  val currentPos = Board.board(startPosition._1)(startPosition._2)
  var currentNode = new Node(currentPos, currentPos)
  openNodes += currentNode

  var targetAchieved = false
  while (openNodes.nonEmpty && !targetAchieved) {

    currentNode = getBestNode
    closedNodes.append(currentNode)

    val possibleSuccessors = getSuccessorsList(currentNode)
    try
      possibleSuccessors.foreach(successor => {
        if ((successor.current.x, successor.current.y) == targetPosition) {
          closedNodes.append(successor)
          throw TargetNodeFoundedException
        }
        successor.current.g += currentNode.current.g
        successor.current.h = estimatedDistance(successor.current)
        successor.current.f = successor.current.g + successor.current.h

        var addToList = true
        openNodes.foreach(openNode => {
          if (openNode.current.x == successor.current.x && openNode.current.y == successor.current.y)
            if (openNode.current.f < successor.current.f)
              addToList = false

        })
        if (addToList)
          closedNodes.foreach(closedNode => {
            if (closedNode.current.x == successor.current.x && closedNode.current.y == successor.current.y)
              if (closedNode.current.f < successor.current.f)
                addToList = false
          })
        if (addToList)
          openNodes.append(successor)
      })
    catch {
      case TargetNodeFoundedException =>
        targetAchieved = true
    }
  }

  def getBestNode: Node = {
    var i = -1
    var betterOne = false

    var bestNode = openNodes.head
    try
      openNodes.foreach(node => {
        i += 1
        if (node.current.f < bestNode.current.f) {
          bestNode = node
          throw NodeFoundedException
        }
      })
    catch {
      case NodeFoundedException =>
        betterOne = true
    }
    if (!betterOne)
      i = 0

    openNodes.remove(i)
  }

  def writeTheLog(logList: ListBuffer[String]): Unit = {
    val fw = new FileWriter(Values.FILE_LOG, true)
    fw.write(Values.START_POS +(startPosition._1, startPosition._2) + Values.TARGET_POS +(targetPosition._1, targetPosition._2) + Values.NEXT_LINE)
    logList.foreach(line => fw.write(line + Values.NEXT_LINE))
    fw.write(Values.WALL)
    fw.close()
  }

  def generatePathMap(): Unit = {
    val foundedPath = setPath().reverse
    val map = Array.ofDim[String](Board.SIZE, Board.SIZE)
    for (xi <- 0 to Board.SIZE - 1; yi <- 0 to Board.SIZE - 1) {
      map(xi)(yi) = Values.E * 3
      closedNodes.foreach(node => {
        if (node.current.x == xi && node.current.y == yi) {
          map(xi)(yi) = Values.S
          foundedPath.foreach(pos => {
            if ((xi, yi) == pos)
              map(xi)(yi) = Values.X
          })
        }
      })
    }

    val logList = new ListBuffer[String]
    var logRow: String = ""

    logList.append(Values.METRIC)
    println(Values.METRIC)
    for (yi <- 0 to Board.SIZE - 1) {
      if (yi < 10) {
        logRow = yi + Values.E * 2
        print(yi + Values.E * 2)
      } else {
        logRow = yi + Values.E
        print(yi + Values.E)
      }
      for (xi <- 0 to Board.SIZE - 1) {
        logRow += map(xi)(yi)
        print(map(xi)(yi))
      }
      logList.append(logRow)
      println()
    }
    logList.append(Values.METRIC)
    println(Values.METRIC)

    writeTheLog(logList)
  }

  def getPath: ListBuffer[Direction] = {
    val foundedPath = setPath().reverse
    var lastPos = foundedPath.remove(0)
    val movementInstruction = new ListBuffer[Direction]()
    foundedPath.foreach(pos => {
      if (pos._1 > lastPos._1)
        movementInstruction.append(EAST)
      else if (pos._1 < lastPos._1)
        movementInstruction.append(WEST)
      else if (pos._2 > lastPos._2)
        movementInstruction.append(SOUTH)
      else
        movementInstruction.append(NORTH)
      lastPos = pos
    })
    movementInstruction
  }

  def setPath(): ListBuffer[(Int, Int)] = {
    closedNodes.foreach(node =>
      println("cN: " +(node.current.x, node.current.y) + " with parent " +(node.parent.x, node.parent.y)))
    val path = new ListBuffer[(Int, Int)]
    //    val target = closedNodes.remove(closedNodes.length - 1)
    val target = closedNodes.last
    path.append(targetPosition)
    var pos = (target.parent.x, target.parent.y)
    while (pos != startPosition) {
      path.append(pos)
      closedNodes.foreach(node => {
        if (pos._1 == node.current.x && pos._2 == node.current.y)
          pos = (node.parent.x, node.parent.y)
      })
    }
    path.append(pos)
    path
  }

  def estimatedDistance(successor: Pos): Double = {
    sqrt(pow(targetPosition._1 - successor.x, 2) + pow(targetPosition._2 - successor.y, 2))
  }

  def getPos(x: Int, y: Int): Pos = {
    Board.board(x)(y)
  }

  def isNotInClosedNodes(succ: Node): Boolean = {
    var notInList = true
    closedNodes.foreach(node =>
      if (succ.current.x == node.current.x && succ.current.y == node.current.y)
        notInList = false)
    notInList
  }

  def getSuccessorsList(parent: Node): ListBuffer[Node] = {
    val px = parent.current.x
    val py = parent.current.y
    val listSuccessors = new ListBuffer[Node]()
    if (px > 0) {
      val westPos = getPos(px - 1, py)
      if (westPos.canEnter) {
        val westNode = new Node(
          parent = parent.current,
          current = westPos
        )
        if (isNotInClosedNodes(westNode)) {
          listSuccessors.append(westNode)
        }
      }
    }
    if (px < Board.SIZE - 1) {
      val eastPos = getPos(px + 1, py)
      if (eastPos.canEnter) {
        val eastNode = new Node(
          parent = parent.current,
          current = eastPos
        )
        if (isNotInClosedNodes(eastNode)) {
          listSuccessors.append(eastNode)
        }
      }
    }
    if (py > 0) {
      val northPos = getPos(px, py - 1)
      if (northPos.canEnter) {
        val northNode = new Node(
          parent = parent.current,
          current = northPos
        )
        if (isNotInClosedNodes(northNode)) {
          listSuccessors.append(northNode)
        }
      }
    }
    if (py < Board.SIZE - 1) {
      val southPos = getPos(px, py + 1)
      if (southPos.canEnter) {
        val southNode = new Node(
          parent = parent.current,
          current = southPos
        )
        if (isNotInClosedNodes(southNode)) {
          listSuccessors.append(southNode)
        }
      }
    }
    listSuccessors
  }
}
