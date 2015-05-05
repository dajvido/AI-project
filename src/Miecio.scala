import gui.AppUI

import obj.Hero


object Miecio {
  def main(args: Array[String]) {
    AppUI.show()
    AppUI.lifestyleTextArea.write("TEST TAG", "STARTING IT YAY!")
    AppUI.lifestyleTextArea.write("TEST2 TAG", "WHAT IS GOING IN HERE?!")
    AppUI.lifestyleTextArea.write("TEST3 TAG", "I'AM ALIVE!!!!!!!!")
    AppUI.lifestyleTextArea.write("TEST4 TAG", "HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEY WORLD!!!!!!!!")

    val miecio = new Hero()
    miecio.equipment += ("Glass" -> 15)
    miecio.equipment += ("Paper" -> 4)
    miecio.equipment += ("Plastic" -> 10)
    miecio.equipment += ("Aluminium" -> 8)
    AppUI.equipmentTextArea.write(miecio.equipment)
  }
}
