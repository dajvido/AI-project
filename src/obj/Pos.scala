package obj

import java.io.File

import scala.util.Random

/**
 * Created by dajvido on 04.05.15.
 */
class Pos(val x : Int,
          val y : Int,
          val canEnter : Boolean = true,
          val isBin : Boolean = false,
          val isShop : Boolean = false,
          val img : File = Pos.BLANK) {
}

object Pos {
  val BLANK = new File("./img/blank.png")
  val HERO = new File("./img/bohater.png")
  val HERO_FULL = new File("./img/bohater1.png")
  val ROBOTS = new File("./img/roboty.png")
  val CAR = new File("./img/samochod.png")
  val TREE = new File("./img/drzewo.png")
  val HOUSE = new File("./img/blok.png")
  val BIN_LOW = new File("./img/kosz0.png")
  val BIN_MEDIUM = new File("./img/kosz1.png")
  val BIN_LARGE = new File("./img/kosz2.png")
  val HERO_BIN_LOW = new File("./img/kosz0l.png")
  val HERO_BIN_MEDIUM = new File("./img/kosz1l.png")
  val HERO_BIN_LARGE = new File("./img/kosz2l.png")
  val SHOP_METAL = new File("./img/sklep1.png")
  val SHOP_GLASS = new File("./img/sklep2.png")
  val SHOP_PAPER = new File("./img/sklep3.png")
  val HERO_SHOP_METAL = new File("./img/sklep1l.png")
  val HERO_SHOP_GLASS = new File("./img/sklep2l.png")
  val HERO_SHOP_PAPER = new File("./img/sklep3l.png")

  var ofHero = Array(0, 0)

  var possible = scala.collection.mutable.HashMap.empty[Int, Int]
  possible += (0 -> 225)
  possible += (1 -> 3)
  possible += (2 -> 3)
  possible += (3 -> 3)
  possible += (4 -> 3)
  possible += (5 -> 3)
  possible += (6 -> 3)
  possible += (7 -> 3)
  possible += (8 -> 3)
  possible += (9 -> 3)
  possible += (10 -> 3)

  var r = Random

  def possibleField(): Int = {
    if (possible(0) > 20) {
      var whatNext = r.nextInt(20)
      if (whatNext > 1) {
        possible(0) -= 1
        return 0
      } else if (possible(0) != possible.values.sum) {
        var n = r.nextInt(10) + 1
        while (possible(n) == 0) {
          n = r.nextInt(11)
        }
        possible(n) -= 1
        return n
      } else {
        possible(0) -= 1
        return 0
      }
    } else {
      var n = r.nextInt(11)
      if (possible(n) != 0) {
        possible(n) -= 1
        return n
      } else if (possible(0) != 0) {
        possible(0) -= 0
        return 0
      } else {
        while (possible(n) == 0) {
          n = r.nextInt(11)
        }
        possible(n) -= 1
        return n
      }
    }
  }

  def binFor(n: Int): Boolean = {
    n match {
      case 4 => true
      case 5 => true
      case 6 => true
      case _ => false
    }
  }

  def shopFor(n: Int): Boolean = {
    n match {
      case 1 => true
      case 2 => true
      case 3 => true
      case _ => false
    }
  }

  def enterableFor(n: Int): Boolean = {
    n match {
      case 7 => false
      case 8 => false
      case 9 => false
      case 10 => false
      case _ => true
    }
  }
  def imageFor(n: Int): File = {
    n match {
      case 1 => SHOP_GLASS
      case 2 => SHOP_METAL
      case 3 => SHOP_PAPER
      case 4 => BIN_LOW
      case 5 => BIN_MEDIUM
      case 6 => BIN_LARGE
      case 7 => ROBOTS
      case 8 => TREE
      case 9 => HOUSE
      case 10 => CAR
      case _ => BLANK
    }
  }
}
