package io.cheng.sharpless.basics
import shapeless.{ Generic, HList }

object Chap1_Concepts_Aux_Pattern extends App {

  /** Aux pattern
    * Aux is just structure helping exposing internal type:
    *
    * let's say we have
    *
    *   sealed trait Foo[A] {
    *     type B
    *     def to(a: A): B
    *   }
    *
    *   object Foo {
    *     type Aux[A0, B0] = new Foo[A0] { type B = B0 }
    *   }
    *
    *   usage: Foo.Aux[A,B]
    *
    * */
  sealed trait Named[A] {
    type B
    def name(a: A): B
  }

  object Named {
    implicit def intNamed: Named[Int] { type B = String } = new Named[Int] {
      override type B = String
      override def name(a: Int): String = "integer"
    }

    type Aux[AA, BB] = Named[AA] { type B = BB }
  }

  sealed trait Show[A] {
    def show(a: A): String
  }

  object Show {
    implicit val stringShow: Show[String] = new Show[String] {
      override def show(a: String): String = a
    }
  }

  /** to demonstrate why Aux pattern is needed, especially in implicit resolution, I have these 2 methods defined */
  def showYourName[A](a: A)(implicit named: Named[A], show: Show[Named[A]#B]): String =
    show show (named name a)

  def showYourNameAlter[A, B](a: A)(implicit named: Named.Aux[A, B], show: Show[B]): String =
    show show (named name a)

  /** it is the compiler limitation: type and its internal type, (A and it's A#B), cannot appear in same brackets */
//  println(showYourName(123))    // -----> Doesn't compile
  /** Aux pattern is a trick to allow internal type exposed as external */
  println(showYourNameAlter(123))

  /** shapeless uses aux pattern to expose the generic representation type
    * */
  def aux[A <: Product, Ar <: HList](a: A)(implicit ev: Generic.Aux[A, Ar]): Ar = ev.to(a)
}
