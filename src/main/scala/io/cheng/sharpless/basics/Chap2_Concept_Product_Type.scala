package io.cheng.sharpless.basics
import shapeless.{ ::, Generic, HNil }

object Chap2_Concept_Product_Type extends App {

  /** Product type is literally like a product of many types
    * say case class Address( number: Int, road: String, post: Post ) is a product type,
    *  it is composition of Int :: String :: Post
    *  existence of Address relies on existence of all Int, String and Post
    * */
  /** In shapeless, `HList` is used to represent a product type --->  heterogeneous list */
  val way1 = 1 :: "Grand Canal Dock" :: HNil
  val way2 = ::(1, ::("Grand Canal Dock", HNil))

  println(s"are 2 ways of construction structurally equal?  >> ${way1 == way2} <<")

  /** the type looks interesting */
  val sampleHList: Int :: String :: HNil = 1 :: "Grand Canal Dock" :: HNil

  /** Shapeless Generic Macro
    * Shapeless Generic[T] macro has 2 methods, `to` and `from`, to transform T to/from HList */
  case class Address(number: Int, road: String)

  val whatsThis: Address = Generic[Address].from(sampleHList)

  /** there is no 1-to-1 relationship, that's why it is generic / shapeless */
  case class Membership(id: Int, name: String)

  val whatIsThisThen: Membership = Generic[Membership].from(sampleHList)

  val addressInstance = Address(number = 1, road = "Sales Road")

  /** an `address` instance becomes a `membership` instance */
  Generic[Membership].from(Generic[Address].to(addressInstance))

  object MoreProductTypes {

    /** Tuple is case class, List is case class, that's why they could have a generic representation derived */
    Generic[(Int, String)]
    Generic[List[Int]]
    Generic[Either[Int, String]]
    Generic[Some[Int]]
  }

  object NoneProductTypes {

    /** but Set is not implemented with case class ::(h, t) */
    //    Generic[Set[String]] // doesn't compile

    /** scala native String is java String,
      * although there is an implicit conversion of `StringOps` to make it looks like operating on Seq[char],
      * still, String is not List / Case class */
    //    Generic[String] // doesn't compile

    //    Generic[Int] // doesn't compile

  }
}
