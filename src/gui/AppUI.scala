package gui

import java.awt.Font

import obj.{Values, Time, Board}

import scala.swing.{SimpleSwingApplication, _}


object AppUI extends SimpleSwingApplication {
  def show(unit: Unit) = { top.visible = true }

  val lifestyleTextArea = new LifestyleArea
  val equipmentTextArea = new EquipmentArea
  val boardPanel = new BoardPanel
  val board = new Board(boardPanel)

  val clock = new Label(Values.TIME_ZERO) {
    foreground = new Color(0, 0, 160)
    font = new Font(Values.FONT_SANSSERIF, Font.PLAIN, 14)
  }

  val buttons = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(0, 0, 10, 0)
    contents += Button(Values.BUTTON_START) { startIt() }
    contents += Button(Values.BUTTON_STOP) { stopIt() }
    contents += Swing.HGlue
    contents += clock
    contents += Swing.HGlue
    contents += Button(Values.TOWARDS_NORTH) { board.move(Values.TOWARDS_NORTH) }
    contents += Button(Values.TOWARDS_WEST) { board.move(Values.TOWARDS_WEST) }
    contents += Button(Values.TOWARDS_SOUTH) { board.move(Values.TOWARDS_SOUTH) }
    contents += Button(Values.TOWARDS_EAST) { board.move(Values.TOWARDS_EAST) }
  }

  def top = new MainFrame {
    title = Values.TITLE
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
        case e: java.lang.InterruptedException => lifestyleTextArea.write(Values.TAG_INTERRUPTED, Values.VAL_INTERRUPTED)
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
