package gui

import java.awt.Dimension

import scala.swing.{Swing, TextArea}


class LifestyleArea extends TextArea {
  preferredSize = new Dimension(580, 230)
  border = Swing.EmptyBorder(10, 10, 10, 10)
  editable = false

  def write(tag : String, msg : String) = {
    text += tag + " : " + msg + "\n"
  }
}
