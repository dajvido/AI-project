package gui

import java.awt.{Color, Dimension, Graphics2D}
import javax.imageio.ImageIO

import obj.{Board, Pos}

import scala.swing.{Panel, Swing}


class BoardPanel extends Panel {
  private val POS_SIZE = 32

  minimumSize = new Dimension(obj.Board.SIZE * POS_SIZE,
    obj.Board.SIZE * POS_SIZE)
  maximumSize = new Dimension(obj.Board.SIZE * POS_SIZE,
    obj.Board.SIZE * POS_SIZE)
  background = new Color(0, 0, 0)
  border = Swing.EmptyBorder(10, 10, 10, 10)

  override def paintComponent(g: Graphics2D): Unit = {
    Board.board.foreach(_.foreach(drawTile(g, _)))
  }

  private def drawTile(g: Graphics2D, pos: Pos): Unit = {
    val image = ImageIO.read(pos.img)
    g.drawImage(image, pos.x * POS_SIZE,
      pos.y * POS_SIZE, null)
  }
}
