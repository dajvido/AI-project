package obj.astar

import java.io.FileWriter

import obj.Direction._
import obj.astar.exceptions.TargetNodeFoundedException
import obj.{Board, Pos, Values}

import scala.collection.mutable.ListBuffer
import scala.math.abs


class AStar(val startPosition: (Int, Int), val targetPosition: (Int, Int)) {

  var openNodes = new ListBuffer[Node]()
  var closedNodes = new ListBuffer[Node]()

  val currentPos = Board.board(startPosition._1)(startPosition._2)
  var currentNode = new Node(current = currentPos)
  openNodes += currentNode

  var targetAchieved = false
  while (openNodes.nonEmpty && !targetAchieved && isDifferentStartAndTargetPos) {

    currentNode = getBestNode
    closedNodes.append(currentNode)

    val possibleSuccessors = getSuccessorsList(currentNode)
    try
      possibleSuccessors.foreach(successor => {
        if ((successor.current.x, successor.current.y) == targetPosition) {
          closedNodes.append(successor)
          throw TargetNodeFoundedException
        }
        var nodeInOpenList = false
        openNodes.foreach(node => {
          if (node.current.x == successor.current.x && node.current.y == successor.current.y) {
            nodeInOpenList = true
            if (node.current.g > successor.current.g) {
              node.parent = currentNode
              node.current.g = successor.current.g + currentNode.current.g
              node.current.f = node.current.g + node.current.h
            }
          }
        })
        if (!nodeInOpenList) {
          successor.current.g += currentNode.current.g
          successor.current.h = estimatedDistance(successor.current)
          successor.current.f = successor.current.g + successor.current.h
          openNodes.append(successor)
        }
      })
    catch {
      case TargetNodeFoundedException =>
        targetAchieved = true
    }
  }

  def isDifferentStartAndTargetPos: Boolean = {
    startPosition != targetPosition
  }

  def getBestNode: Node = openNodes.remove(openNodes.indexOf(openNodes.minBy(node => node.current.f)))

  def getSuccessorsList(parent: Node): ListBuffer[Node] = {
    val px = parent.current.x
    val py = parent.current.y
    val listSuccessors = new ListBuffer[Node]()
    if (px > 0) {
      val availableNode = getNodeOrNull(getPos(px - 1, py), parent)
      if (availableNode.isInstanceOf[Node])
        listSuccessors.append(availableNode)
    }
    if (px < Board.SIZE - 1) {
      val availableNode = getNodeOrNull(getPos(px + 1, py), parent)
      if (availableNode.isInstanceOf[Node])
        listSuccessors.append(availableNode)
    }
    if (py > 0) {
      val availableNode = getNodeOrNull(getPos(px, py - 1), parent)
      if (availableNode.isInstanceOf[Node])
        listSuccessors.append(availableNode)
    }
    if (py < Board.SIZE - 1) {
      val availableNode = getNodeOrNull(getPos(px, py + 1), parent)
      if (availableNode.isInstanceOf[Node])
        listSuccessors.append(availableNode)
    }
    listSuccessors
  }

  def getPos(x: Int, y: Int): Pos = {
    Board.board(x)(y)
  }

  val move = (direction: Direction) => Board.move(direction)

  def whichDirection(lastPos: Pos, newPos: Pos): Direction = {
    if (newPos.x > lastPos.x)
      EAST
    else if (newPos.x < lastPos.x)
      WEST
    else if (newPos.y > lastPos.y)
      SOUTH
    else
      NORTH
  }

  def getNodeOrNull(checkPos: Pos, parentNode: Node): Node = {
    if (checkPos.canEnter) {
      val availableNode = new Node(
        parent = parentNode,
        current = checkPos,
        action = () => move(whichDirection(parentNode.current, checkPos))
      )
      if (isNotInClosedNodes(availableNode)) {
        return availableNode
      }
    }
    null
  }

  def isNotInClosedNodes(succ: Node): Boolean = {
    var notInList = true
    closedNodes.foreach(node =>
      if (succ.current.x == node.current.x && succ.current.y == node.current.y)
        notInList = false)
    notInList
  }

  def estimatedDistance(successor: Pos): Double = {
    abs(targetPosition._1 - successor.x) + abs(targetPosition._2 - successor.y)
  }

  def shouldAddToOpenList(successor: Node): Boolean = {
    openNodes.foreach(openNode => {
      if (openNode.current.x == successor.current.x && openNode.current.y == successor.current.y)
        if (openNode.current.f < successor.current.f)
          return false

    })
    closedNodes.foreach(closedNode => {
      if (closedNode.current.x == successor.current.x && closedNode.current.y == successor.current.y)
        if (closedNode.current.f < successor.current.f)
          return false
    })
    true
  }

  def writeToLog(logList: ListBuffer[String]): Unit = {
    val fw = new FileWriter(Values.FILE_LOG, true)
    fw.write(Values.START_POS +(startPosition._1, startPosition._2) + Values.TARGET_POS +(targetPosition._1, targetPosition._2) + Values.NEXT_LINE)
    logList.foreach(line => fw.write(line + Values.NEXT_LINE))
    fw.write(Values.WALL)
    fw.close()
  }

  def getMapArrayOfNodeF: Array[Array[String]] = {
    val map = Array.ofDim[String](Board.SIZE, Board.SIZE)
    for (xi <- 0 to Board.SIZE - 1; yi <- 0 to Board.SIZE - 1) {
      val d = Board.board(xi)(yi).f
      map(xi)(yi) = if (d < 10) d.toInt + "  " else d.toInt + " "
    }
    map
  }

  def generateTheLog(map: Array[Array[String]]): ListBuffer[String] = {
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
    logList.append(Values.NEXT_LINE)
    println(Values.NEXT_LINE)
    logList
  }

  def generatePathMap(): Unit = {
    if (isDifferentStartAndTargetPos) {
      val logCostList = generateTheLog(getMapArrayOfNodeF)

      writeToLog(logCostList)
    }
  }

  def clearPathCost(): Unit = {
    var field = 0
    for (xi <- 0 to Board.SIZE - 1; yi <- 0 to Board.SIZE - 1) {
      field = Pos.mapField(xi, yi)
      Board.board(xi)(yi).g = Pos.moveCost(field)
      Board.board(xi)(yi).h = 0.0
      Board.board(xi)(yi).f = 0.0
    }
  }

  def getPath: ListBuffer[() => Unit] = {
    clearPathCost()
    var movementInstruction = new ListBuffer[() => Unit]()
    if (isDifferentStartAndTargetPos) {
      movementInstruction = getActions
    }
    movementInstruction
  }

  def getActions: ListBuffer[() => Unit] = {
    val actions = new ListBuffer[() => Unit]

    val target = closedNodes.last
    actions.append(target.action)

    var parent = target.parent

    while (startPosition !=(parent.current.x, parent.current.y)) {
      actions.append(parent.action)
      closedNodes.foreach(node => {
        if (parent.current.x == node.current.x && parent.current.y == node.current.y)
          parent = node.parent
      })
    }
    actions.reverse
  }

  def getCost: Double = {
    var cost = 0.0

    val target = closedNodes.last
    cost = cost + target.current.g

    var parent = target.parent

    while (startPosition !=(parent.current.x, parent.current.y)) {
      cost = cost + parent.current.g
      closedNodes.foreach(node => {
        if (parent.current.x == node.current.x && parent.current.y == node.current.y)
          parent = node.parent
      })
    }
    cost
  }

  def getPathCost: Double = {
    var cost = 0.0
    clearPathCost()
    if (isDifferentStartAndTargetPos) {
      cost = getCost
    }
    cost
  }
}
