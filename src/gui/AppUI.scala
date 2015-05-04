package gui

import obj.Board

import scala.swing.{SimpleSwingApplication, _}

/**
 * Created by dajvido on 04.05.15.
 */
object AppUI extends SimpleSwingApplication {
  def show(unit: Unit) = { top.visible = true }


  val lifestyleTextArea = new LifestyleArea
  val equipmentTextArea = new EquipmentArea
  val boardPanel = new BoardPanel
  val board = new Board(boardPanel)
  val buttons = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(0, 0, 10, 0)
    contents += Button("Start") { startIt() }
    contents += Button("Stop") { stopIt() }
    contents += Swing.HGlue
    contents += Button("Move North") { board.move("NORTH") }
    contents += Button("Move West") { board.move("WEST") }
    contents += Button("Move South") { board.move("SOUTH") }
    contents += Button("Move East") { board.move("EAST") }
  }

  def top = new MainFrame {
    title = "AI-Project"
    minimumSize = new Dimension(800, 600)

    contents = new BorderPanel {
      border = Swing.EmptyBorder(10, 10, 10, 10)
      add(buttons, BorderPanel.Position.North)
      add(boardPanel, BorderPanel.Position.Center)
      add(lifestyleTextArea, BorderPanel.Position.South)
      add(equipmentTextArea, BorderPanel.Position.East)
    }
  }

  def startIt(): Unit = {

  }

  def stopIt(): Unit = {

  }
}
