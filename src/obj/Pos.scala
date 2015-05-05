package obj

import java.io.File

import scala.collection.mutable._


class Pos(val x : Int,
          val y : Int,
          val canEnter : Boolean = true,
          val isBin : Boolean = false,
          val isShop : Boolean = false,
          val img : File = Pos.BLANK) {
}

object Pos {
  val BLANK = new File(Values.FILE_BLANK)
  val HERO = new File(Values.FILE_HERO)
  val HERO_FULL = new File(Values.FILE_HERO_FULL)
  val ROBOTS = new File(Values.FILE_ROBOTS)
  val CAR = new File(Values.FILE_CAR)
  val TREE = new File(Values.FILE_TREE)
  val HOUSE = new File(Values.FILE_HOUSE)
  val BIN_LOW = new File(Values.FILE_BIN_LOW)
  val BIN_MEDIUM = new File(Values.FILE_BIN_MEDIUM)
  val BIN_LARGE = new File(Values.FILE_BIN_LARGE)
  val HERO_BIN_LOW = new File(Values.FILE_HERO_BIN_LOW)
  val HERO_BIN_MEDIUM = new File(Values.FILE_HERO_BIN_MEDIUM)
  val HERO_BIN_LARGE = new File(Values.FILE_HERO_BIN_LARGE)
  val SHOP_METAL = new File(Values.FILE_SHOP_METAL)
  val SHOP_GLASS = new File(Values.FILE_SHOP_GLASS)
  val SHOP_PAPER = new File(Values.FILE_SHOP_PAPER)
  val HERO_SHOP_METAL = new File(Values.FILE_HERO_SHOP_METAL)
  val HERO_SHOP_GLASS = new File(Values.FILE_HERO_SHOP_GLASS)
  val HERO_SHOP_PAPER = new File(Values.FILE_HERO_SHOP_PAPER)

  var ofHero = Array(0, 0)

  var exampleMap = HashMap.empty[(Int, Int), Int]
  exampleMap += ((0,0) -> 0)
  exampleMap += ((2,0) -> 4)
  exampleMap += ((8,0) -> 8)
  exampleMap += ((9,0) -> 8)
  exampleMap += ((11,0) -> 7)
  exampleMap += ((12,0) -> 10)
  exampleMap += ((14,0) -> 3)
  exampleMap += ((4,1) -> 8)
  exampleMap += ((7,1) -> 8)
  exampleMap += ((8,1) -> 8)
  exampleMap += ((9,1) -> 8)
  exampleMap += ((4,2) -> 8)
  exampleMap += ((4,3) -> 10)
  exampleMap += ((10,3) -> 1)
  exampleMap += ((4,4) -> 8)
  exampleMap += ((8,4) -> 6)
  exampleMap += ((4,5) -> 10)
  exampleMap += ((4,6) -> 10)
  exampleMap += ((13,6) -> 6)
  exampleMap += ((1,7) -> 9)
  exampleMap += ((2,7) -> 9)
  exampleMap += ((3,7) -> 9)
  exampleMap += ((4,7) -> 9)
  exampleMap += ((5,7) -> 5)
  exampleMap += ((9,8) -> 8)
  exampleMap += ((10,8) -> 8)
  exampleMap += ((11,8) -> 8)
  exampleMap += ((2,9) -> 2)
  exampleMap += ((11,9) -> 7)
  exampleMap += ((12,9) -> 10)
  exampleMap += ((3,10) -> 5)
  exampleMap += ((11,10) -> 7)
  exampleMap += ((12,10) -> 6)
  exampleMap += ((0,11) -> 3)
  exampleMap += ((11,11) -> 7)
  exampleMap += ((0,12) -> 8)
  exampleMap += ((5,12) -> 4)
  exampleMap += ((8,12) -> 8)
  exampleMap += ((14,12) -> 6)
  exampleMap += ((0,13) -> 6)
  exampleMap += ((2,14) -> 1)
  exampleMap += ((3,14) -> 10)
  exampleMap += ((12,14) -> 2)

  def shopFor(n: Int): Boolean = {
    n match {
      case 1 => true
      case 2 => true
      case 3 => true
      case _ => false
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
      case 0 => HERO
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
