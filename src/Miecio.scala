import gui.AppUI

import obj.{Values, Hero}


object Miecio {
  def main(args: Array[String]) {
    AppUI.show()
    AppUI.lifestyleTextArea.write(Values.TAG_TEST, Values.VAL_TEST)
    AppUI.lifestyleTextArea.write(Values.TAG_TEST, Values.VAL_TEST)
    AppUI.lifestyleTextArea.write(Values.TAG_TEST, Values.VAL_TEST)

    val miecio = new Hero()
    miecio.equipment += (Values.ITEM_GLASS -> 15)
    miecio.equipment += (Values.ITEM_PAPER -> 4)
    miecio.equipment += (Values.ITEM_PLASTIC -> 10)
    miecio.equipment += (Values.ITEM_ALUMINIUM -> 8)
    AppUI.equipmentTextArea.write(miecio.equipment)
  }
}
