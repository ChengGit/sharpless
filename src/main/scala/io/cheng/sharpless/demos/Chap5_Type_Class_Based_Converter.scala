package io.cheng.sharpless.demos

import shapeless.{ ::, Generic, HList, HNil, Lazy }

/** In this chapter we are going to define typeclass(es) to convert Digital instance to a Platform instance */
object Chap5_Type_Class_Based_Converter extends App {

  /** from previous chapter,
    * we know we are able to convert a HList representation of Digital into a HList representation of Platform
    * And from `basics/ProductType`, and `Generic[T]`,
    * we know we are able to convert between HList and case class
    *
    * So, to complete the logic chain, we could design some typeclasses:
    *   1. transform Digital into HList
    *   2. convert HList to a Platform representation
    *   3. transform back to Platform
    * */
  /** FieldConverter is to convert each of the field in HList, to its desired type/value */
  sealed trait FieldConverter[A] {
    type B
    def convert(a: A): B
  }

  object FieldConverter {

    /** Aux pattern as we went through in `basic/AuxPattern`,
      * here is a real world use case, to expose the converted type
      * */
    type Aux[AA, BB] = FieldConverter[AA] { type B = BB }

    def apply[A](implicit ev: FieldConverter[A]): FieldConverter[A] = ev

    implicit def intConverter: FieldConverter[Int] { type B = Int } = new FieldConverter[Int] {
      type B = Int
      override def convert(a: Int): B = a
    }

    implicit def strConverter: FieldConverter[String] { type B = String } = new FieldConverter[String] {
      type B = String
      override def convert(a: String): B = "paas development"
    }

    /** when dealing with HList, there are 2 cases that should be taken care of:
      * 1. how do deal with a non-empty HList
      * 2. how to deal with empty HList, i.e. HNil
      * */
    implicit def hnilConverter: FieldConverter[HNil] { type B = HNil } = new FieldConverter[HNil] {
      type B = HNil
      override def convert(a: HNil): B = a
    }

    /** Here, `FieldConverter.Aux[AA, BB]` is used to expose converted type
      * `Ah` and `At` represents the type of head and tail of HListA =>   (head : Ah) :: (tail :: At)
      * so it is with `Bh` and `Bt`
      *
      *   Note: `shapeless.Lazy` is used to prevent overflow,
      *   one example:
      *     `tConv` of `hlistConverter` will always find itself as implicit parameter, which could cause overflow.
      *
      *     more details can be found: https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/lazy.scala
      * */
    implicit def hlistConverter[Ah, At <: HList, Bh, Bt <: HList](
      implicit
      hConv: Lazy[FieldConverter.Aux[Ah, Bh]],
      tConv: Lazy[FieldConverter.Aux[At, Bt]]
    ): FieldConverter[Ah :: At] { type B = Bh :: Bt } = new FieldConverter[Ah :: At] {
      override type B = Bh :: Bt
      override def convert(a: Ah :: At): Bh :: Bt = a match {
        case h :: t => hConv.value.convert(h) :: tConv.value.convert(t)
      }
    }
  }

  /** Time to check if FieldConverter[T] can convert given hlist*/
  println(FieldConverter[Int :: String :: HNil].convert(1 :: "Some String" :: HNil))

  /** `FieldConverter[T]` converts each of field in recursive way. */
  /** Next, we create a typeclass that works on a higher abstraction: `CaseClassConverter[A, B]` */
  sealed abstract class CaseClassConverter[A, B] {

    def convert[Ar <: HList, Br <: HList](
      a: A
    )(implicit agen: Generic.Aux[A, Ar], bgen: Generic.Aux[B, Br], fc: FieldConverter.Aux[Ar, Br]): B
  }

  object CaseClassConverter {
    def apply[A, B](implicit agen: Generic[A], bgen: Generic[B], ev: CaseClassConverter[A, B]) = ev

    /** Note that `agen` and `bgen` are not used */
    implicit def caseClass[A, B](implicit agen: Generic[A], bgen: Generic[B]) =
      new CaseClassConverter[A, B]() {
        override def convert[Ar <: HList, Br <: HList](
          a: A
        )(
          implicit agen: Generic.Aux[A, Ar],
          bgen: Generic.Aux[B, Br],
          fc: FieldConverter.Aux[Ar, Br]
        ): B = bgen.from(fc.convert(agen.to(a)))
      }

  }

  val digitalInstance: Digital            = Digital(id = 1, job = "software development")
  val digitalHList: Int :: String :: HNil = Generic.apply[Digital].to(digitalInstance)

  println(CaseClassConverter[Digital, Platform].convert(digitalInstance))
}
