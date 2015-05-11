package obj

import scala.collection.mutable.ListBuffer


object Decision {
  def sortByDistance(fieldsToSort: ListBuffer[Bin]): ListBuffer[Bin] = {
    // TODO: sort fieldsToSort
    fieldsToSort
  }

  def whichBin(availableBins: ListBuffer[Bin]): Bin = {
    val sortedBins = sortByDistance(availableBins)
    // TODO: if pelny, w polowie pelny, niewiadomy, else
  }
}
