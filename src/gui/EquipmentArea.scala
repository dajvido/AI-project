package gui

import java.awt.Dimension

import scala.collection.mutable.HashMap
import scala.swing.{Swing, TextArea}

/**
 * Created by dajvido on 04.05.15.
 */
class EquipmentArea extends TextArea {
  preferredSize = new Dimension(400, 380)
  border = Swing.EmptyBorder(10, 10, 10, 10)
  editable = false

  def write(items : HashMap[String, Int]) = {
    text = ""
    items.foreach(item => text +=  "Have " + item._2.toString() + " pieces of " + item._1 + "\n")
  }
}
