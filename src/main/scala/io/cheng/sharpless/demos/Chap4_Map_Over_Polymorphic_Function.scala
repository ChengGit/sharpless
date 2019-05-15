package io.cheng.sharpless.demos
import shapeless.ops.hlist.Mapper
import shapeless.{ ::, Generic, HList, HNil, Poly, Poly1 }

object Chap4_Map_Over_Polymorphic_Function extends App {
  val digitalInstance = Digital(id = 1, job = "software development")

  val platformInstance = Platform(id = 1, job = "paas development")

  val digitalHList: Int :: String :: HNil = Generic[Digital].to(digitalInstance)

  /** we want design a function that maps by type */
//    Generic[Platform]
//      .from(
//        Generic[Digital].to(digitalInstance).map( ??? )
//      )

  /** Introduction ! shapeless.Poly
    *
    * Polymorphic functions is defined on type level*/
  object myPolymorphicFunc extends Poly1 {

    /** `Poly1` represents functions A => B,
      * `Poly2` represents functions [A1, A2] => B,
      * etc..
      * our function applies on each of the field of `Int :: String :: HList`, so here we inherent `Poly1` */
    implicit val intCase: Case.Aux[Int, Int]          = at(identity[Int])
    implicit val stringCase: Case.Aux[String, String] = at(_ => "paas development")
  }

  val yeahItWorks: Platform =
    Generic[Platform].from(Generic[Digital].to(digitalInstance).map(myPolymorphicFunc))
  println(s"printing the platform instance converted by Poly Function: $yeahItWorks")

  /** yeah it works, that's it thanks*/
  /** make it more generic */
  /** we create typeclass for conversion */
  sealed trait PolyConverter[A, B, P] {
    def convert(a: A): B
  }

  object PolyConverter {
    def apply[A, B, P <: Poly](implicit ev: PolyConverter[A, B, P]): PolyConverter[A, B, P] = ev

    implicit def withPolyConverter[A, Ar <: HList, B, Br <: HList, P <: Poly](
      implicit
      agen: Generic.Aux[A, Ar],
      bgen: Generic.Aux[B, Br],
      mapper: Mapper.Aux[P, Ar, Br]
    ): PolyConverter[A, B, P] = new PolyConverter[A, B, P] {
      override def convert(a: A): B = bgen.from(mapper.apply(agen.to(a)))
    }
  }

  val withParametricTypes = PolyConverter[Digital, Platform, myPolymorphicFunc.type].convert(digitalInstance)
  println(s"printing the platform instance converted by type class `PolyConverter` $withParametricTypes")

}
