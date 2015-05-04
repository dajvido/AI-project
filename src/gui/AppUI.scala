package gui

import java.awt.Font

import obj.Time

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

  val clock = new Label("00:00") {
    foreground = new Color(0, 0, 160)
    font = new Font("SansSerif", Font.PLAIN, 14)
  }

  val buttons = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(0, 0, 10, 0)
    contents += Button("Start") { startIt() }
    contents += Button("Stop") { stopIt() }
    contents += Swing.HGlue
    contents += clock
    contents += Swing.HGlue
    contents += Button("Move North") { board.move("NORTH") }
    contents += Button("Move West") { board.move("WEST") }
    contents += Button("Move South") { board.move("SOUTH") }
    contents += Button("Move East") { board.move("EAST") }
  }

  def top = new MainFrame {
    title = "AI-Project"
    minimumSize = new Dimension(800, 800)

    contents = new BorderPanel {
      border = Swing.EmptyBorder(10, 10, 10, 10)
      add(buttons, BorderPanel.Position.North)
      add(boardPanel, BorderPanel.Position.Center)
      add(lifestyleTextArea, BorderPanel.Position.South)
      add(equipmentTextArea, BorderPanel.Position.East)
    }
  }

  def tick() {
    clock.text = Time.displayTime
  }

  val thread = new Thread {
    override def run: Unit = {
      try {
        while(true) {
          Thread.sleep(100)
          tick()
        }
      } catch {
        case e: java.lang.InterruptedException => lifestyleTextArea.write("TIME THREAD", "INTERUPT")
      }
    }
  }

  def startIt(): Unit = {
    if (!thread.isAlive())
      thread.start()
  }

  def stopIt(): Unit = {
    thread.interrupt()
  }
}
