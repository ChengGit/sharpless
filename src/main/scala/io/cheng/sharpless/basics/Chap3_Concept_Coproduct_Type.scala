package io.cheng.sharpless.basics
import shapeless.{ :+:, CNil, Generic, Inl, Inr }
object Chap3_Concept_Coproduct_Type extends App {

  /** Coproduct type represent OR relationship, similar to algebraic data type(ADT):
    * Let's say
    *  sealed trait Results
    *   case class Correct() extends Results
    *   case class Wrong()   extends Results
    * existence of Coproduct rely on one of its type exists:  Correct() :+:(OR) Wrong()
    * */
  /** Shapeless CList represent this relationship,
    * at typelevel, with symbol `:+:`
    * at instance level, with symbol `Inl` and `Inr` */
  object CListRepresentationOfADT {

    case class Correct()
    case class Wrong()
    type Result = Correct :+: Wrong :+: CNil

    /** manually draft an instance of a CList, step by step,
      * loosely similar to nested `either`,
      * where `Either.Left` is similar to `Inl` and `Either.Right` is similar to `Inr` */
    val checkMyType1: Wrong :+: CNil =
      Inl[Wrong, CNil](Wrong())

    val checkMyType2: Correct :+: Wrong :+: CNil =
      Inr[Correct, Wrong :+: CNil](Inl[Wrong, CNil](Wrong()))

    val checkMyType2_AlternativeTypeSignature: Result =
      Inr[Correct, Wrong :+: CNil](Inl[Wrong, CNil](Wrong()))

  }

  /** to demonstrate derive CList from ADT, this time we make Correct and Wrong an ADT */
  object DerivingCListFromADT {
    sealed trait Results
    case class Correct() extends Results
    case class Wrong()   extends Results

    type Result = Correct :+: Wrong :+: CNil

    val handcraftedCList: Result =
      Inr[Correct, Wrong :+: CNil](Inl[Wrong, CNil](Wrong()))

    /** for coproduct, intellij scala plugin cannot correctly infer the type of generic representation of a Coproduct */
    val derivedWrong = Generic[Results].to(Wrong())

    println(s"this is derived instance of Wrong: $derivedWrong")

    /** to demonstrate CList can be converted back to ADT */
    println(Generic[Results].from(derivedWrong))
    println(Generic[Results].from(handcraftedCList))

    println(s"is derived identical to the handcrafted? >> ${derivedWrong == handcraftedCList} <<")
  }
}
