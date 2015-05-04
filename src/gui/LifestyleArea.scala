package gui

import java.awt.Dimension

import scala.swing.{Swing, TextArea}

/**
 * Created by dajvido on 04.05.15.
 */
class LifestyleArea extends TextArea {
  preferredSize = new Dimension(580, 220)
  border = Swing.EmptyBorder(10, 10, 10, 10)
  editable = false

  def write(tag : String, msg : String) = {
    text += tag + " : " + msg + "\n"
  }
}
