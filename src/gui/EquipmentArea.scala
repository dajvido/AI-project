package gui

import java.awt.Dimension

import scala.collection.mutable.HashMap
import scala.swing.{Swing, TextArea}


class EquipmentArea extends TextArea {
  preferredSize = new Dimension(285, 380)
  border = Swing.EmptyBorder(10, 10, 10, 10)
  editable = false

  def write(items : HashMap[String, Int]) = {
    text = ""
    items.foreach(item => text +=  "Have " + item._2.toString() + " pieces of " + item._1 + "\n")
  }
}
