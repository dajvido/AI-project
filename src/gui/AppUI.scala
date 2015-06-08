package gui

import java.awt.Font

import obj._

import scala.swing.{SimpleSwingApplication, _}


object AppUI extends SimpleSwingApplication {
  def show(unit: Unit) = {
    top.visible = true
  }

  val lifestyleTextArea = new LifestyleArea
  val equipmentTextArea = new EquipmentArea

  val clock = new Label(Values.TIME_ZERO) {
    foreground = new Color(0, 0, 160)
    font = new Font(Values.FONT_SANS_SERIF, Font.PLAIN, 14)
  }

  val buttons = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(0, 0, 10, 0)
    contents += Button(Values.BUTTON_START) {
      startIt()
    }
    contents += Button(Values.BUTTON_STOP) {
      stopIt()
    }
    contents += Swing.HGlue
    contents += clock
    contents += Swing.HGlue
    contents += Button(Direction.NORTH.toString) {
      Board.move(Direction.NORTH)
    }
    contents += Button(Direction.WEST.toString) {
      Board.move(Direction.WEST)
    }
    contents += Button(Direction.SOUTH.toString) {
      Board.move(Direction.SOUTH)
    }
    contents += Button(Direction.EAST.toString) {
      Board.move(Direction.EAST)
    }
    contents += Button("(6,9)") {
      Board.costTo((6, 9))
      Board.moveTo((6, 9))
    }
    contents += Button("(0,0)") {
      Board.costTo((0, 0))
      Board.moveTo((0, 0))
    }
    contents += Button("(14,9)") {
      Board.costTo((14, 9))
      Board.moveTo((14, 9))
    }
  }

  def top = new MainFrame {
    title = Values.TITLE
    minimumSize = new Dimension(800, 800)

    contents = new BorderPanel {
      border = Swing.EmptyBorder(10, 10, 10, 10)
      add(buttons, BorderPanel.Position.North)
      add(Board.panel, BorderPanel.Position.Center)
      add(lifestyleTextArea, BorderPanel.Position.South)
      add(equipmentTextArea, BorderPanel.Position.East)
    }
  }

  def tick() {
    clock.text = Time.displayTime
  }

  val thread = new Thread {
    override def run(): Unit = {
      try {
        while (true) {
          Thread.sleep(100)
          tick()
        }
      } catch {
        case e: java.lang.InterruptedException => lifestyleTextArea.write(Values.TAG_INTERRUPTED, Values.VAL_INTERRUPTED)
      }
    }
  }

  def startIt(): Unit = {
    if (!thread.isAlive)
      thread.start()
  }

  def stopIt(): Unit = {
    thread.interrupt()
  }
}
