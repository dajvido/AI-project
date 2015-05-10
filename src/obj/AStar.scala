package obj

import scala.collection.mutable.ListBuffer
import scala.math.abs

//class Node(val px: Int, val py: Int, val x: Int, val y: Int, val g: Float, val h: Float, val f: Float) {
//
//}
class Node(val parent: Pos, val current: Pos) {}

object TargetFounded extends Exception {}

class AStar(val startPosition: (Int, Int), val targetPosition: (Int, Int)) {

  var openNodes = new ListBuffer[Node]()
  var closedNodes = new ListBuffer[Node]()

  val currentPos = Board.board(startPosition._1)(startPosition._2)
  var currentNode = new Node(currentPos, currentPos)
  openNodes += currentNode

  var targetAchievied = false
  while (openNodes.nonEmpty && !targetAchievied) {

    currentNode = getBestNode()

    val possibleSuccessors = getSuccessorsList(currentNode)
    try {
      possibleSuccessors.foreach(successor => {
        if ((successor.current.x, successor.current.y) == targetPosition) {
          closedNodes.append(successor)
          println("YEY")
          throw TargetFounded
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
      closedNodes.append(currentNode)
      println(":* " + currentNode.current.x + ", " + currentNode.current.y)
      //      println("Closed Nodes")
      //      closedNodes.foreach(n => {
      //        println(n.current.x + ", " + n.current.y)
      //      })
      //      println("Open Nodes")
      //      openNodes.foreach(n => {
      //        println(n.current.x + ", " + n.current.y)
      //      })
      //      println("=========================================")
    } catch {
      case TargetFounded => {
        println(currentNode.current.x + " " + currentNode.current.y)
        targetAchievied = true
      }
    }
  }

  def getBestNode(): Node = {
    var i = -1
    var inc = true

    var bestNode = openNodes.apply(0)
    openNodes.foreach(node => {
      if (inc)
        i += 1
      if (node.current.f < bestNode.current.f) {
        bestNode = node
        inc = false
      }
    })
    openNodes.remove(i)
  }

  def getPath(): Unit = {
    val foundedPath = setPath()
    foundedPath.foreach(pos => {
      println("(" + pos._1 + "," + pos._2 + ")")
    })
  }

  def setPath(): ListBuffer[(Int, Int)] = {
    val path = new ListBuffer[(Int, Int)]
    val target = closedNodes.remove(closedNodes.length - 1)
    //    println(target.current.x, target.current.y)
    //    println(target.parent.x, target.parent.y)
    path.append(targetPosition)
    var pos = (target.parent.x, target.parent.y)
    while (pos != startPosition) {
      //    println(pos)
      //      println("(" + pos._1 + "," + pos._2 + ") vs (" + startPosition._1 + "," + startPosition._2 + ")")
      path.append(pos)
      closedNodes.foreach(node => {
        //        println((node.current.x, node.current.y))
        //        println(pos._1 + "vs" + node.current.x + " " + (pos._1 == node.current.x).toString + " : " + pos._2 + "vs" + node.current.y + " " + (pos._2 == node.current.y).toString)
        //        println(pos._2 + " vs " + node.current.y)
        //        println("(" + pos._1 + "," + pos._2 + ") vs (" + node.current.x + "," + node.current.y + ")")
        if (pos._1 == node.current.x && pos._2 == node.current.y)
          pos = (node.parent.x, node.parent.y)
      })
    }
    path
  }

  def estimatedDistance(successor: Pos): Double = {
    abs((successor.x + successor.y) - (targetPosition._1 + targetPosition._2))
  }

  def getPos(x: Int, y: Int): Pos = {
    Board.board(x)(y)
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
        if (!closedNodes.contains(westNode)) {
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
        if (!closedNodes.contains(eastNode)) {
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
        if (!closedNodes.contains(northNode)) {
          listSuccessors.append(northNode)
        }
      }
    }
    if (py < Board.SIZE - 1) {
      val sothPos = getPos(px, py + 1)
      if (sothPos.canEnter) {
        val southNode = new Node(
          parent = parent.current,
          current = sothPos
        )
        if (!closedNodes.contains(southNode)) {
          listSuccessors.append(southNode)
        }
      }
    }
    listSuccessors
  }
}
