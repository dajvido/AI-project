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
        successor.current.g += currentNode.current.g
        successor.current.h = estimatedDistance(successor.current)
        successor.current.f = successor.current.g + successor.current.h

        if (shouldAddToOpenList(successor))
          openNodes.append(successor)
      })
    catch {
      case TargetNodeFoundedException =>
        targetAchieved = true
    }
  }

  def isDifferentStartAndTargetPos: Boolean = {
    startPosition != targetPosition
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

  def getNodeOrNull(checkPos: Pos, parentNode: Node): Node = {
    if (checkPos.canEnter) {
      val availableNode = new Node(
        parent = parentNode.current,
        current = checkPos
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
    sqrt(pow(targetPosition._1 - successor.x, 2) + pow(targetPosition._2 - successor.y, 2))
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

  def getMapArray: Array[Array[String]] = {
    val foundedPath = setPath()
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
    logList.append(Values.METRIC)
    println(Values.METRIC)
    logList
  }

  def generatePathMap(): Unit = {
    if (isDifferentStartAndTargetPos) {
      val map = getMapArray
      val logList = generateTheLog(map)

      writeToLog(logList)
    }
  }

  def setPath(): ListBuffer[(Int, Int)] = {
    val path = new ListBuffer[(Int, Int)]
    val target = closedNodes.last
    var pos = (target.parent.x, target.parent.y)

    path.append(targetPosition)
    while (pos != startPosition) {
      path.append(pos)
      closedNodes.foreach(node => {
        if (pos._1 == node.current.x && pos._2 == node.current.y)
          pos = (node.parent.x, node.parent.y)
      })
    }
    path.append(pos)
    path.reverse
  }

  def findDirection: ListBuffer[Direction] = {
    val movementInstruction = new ListBuffer[Direction]()
    val foundedPath = setPath()
    var lastPos = foundedPath.remove(0)
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

  def getPath: ListBuffer[Direction] = {
    var movementInstruction = new ListBuffer[Direction]()
    if (isDifferentStartAndTargetPos) {
      movementInstruction = findDirection
    }
    movementInstruction
  }
}
