package gui

import java.awt.{Graphics2D, Color, Dimension}
import javax.imageio.ImageIO

import _root_.obj.Pos
import gui.AppUI

import scala.swing.{Swing, Panel}

/**
 * Created by dajvido on 04.05.15.
 */
class BoardPanel extends Panel {
  private val POS_SIZE = 32

  minimumSize = new Dimension(obj.Board.SIZE*POS_SIZE,
    obj.Board.SIZE*POS_SIZE)
  maximumSize = new Dimension(obj.Board.SIZE*POS_SIZE,
    obj.Board.SIZE*POS_SIZE)
  background = new Color(0, 0, 0)
  border = Swing.EmptyBorder(10, 10, 10, 10)


  override def paintComponent(g: Graphics2D): Unit = {
    AppUI.board.board.foreach(_.foreach(drawTile(g, _)))
  }

  private def drawTile(g : Graphics2D, pos : Pos): Unit = {
    val image = ImageIO.read(pos.img)
    g.drawImage(image, pos.x*POS_SIZE,
      pos.y*POS_SIZE, null)
  }
}
