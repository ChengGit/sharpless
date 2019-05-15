package io.cheng.sharpless.problems

import shapeless.{ ::, Generic, HNil }

object Chap0_Problem_Statement {

  case class Digital(id: Int, job: String)

  case class Platform(id: Int, job: String)

  /**
    * our goal is to convert this digital instance to a platform instance
    *
    * */

  val digitalInstance = Digital(id = 1, job = "software development")

  val platformInstance = Platform(id = 1, job = "paas development")

  /**  and
  *  the conversion flow is:
  *
  *
  *  a Digital instance     -------------->       Digital generic representation( HList )
  *                                                             |
  *                                                             |
  *                                                             |
  *                                                             ?
  *                                                             ?
  *                                                             |
  *                                                             |
  *                                                             |
  *  a Platform instance     <--------------       Platform generic representation( HList )
  *
  * */
}

object Intuitive_Attempt extends App {

  import Chap0_Problem_Statement._

  /** Generic[T].to convert a case class T to a generic representation(HList)
    *   Generic[T].from convert a generic representation back to a case class T
    *
    *   NOTE: it will fail compilation if conversion fails
    * */
  val digitalHList: Int :: String :: HNil = Generic[Digital].to(digitalInstance)

  println("hlist of my digital instance looks like:\n " + digitalHList)

  println(
    s"\nis converted case class digital structurally equal to original one?\n " +
      s">> ${Generic[Digital].from(digitalHList) == digitalInstance} <<"
  )

  /** moreover,
    * any HList that fits the type in same order of Digital can be converted to a digital instance
    * */
  val myHList: Int :: String :: HNil = digitalInstance.id :: digitalInstance.job :: HNil

  /** compilation will fail if `myHList` cannot be converted to a Digital case class instance*/
  println(s"\nthis digital instance comes from `myHList` : ${Generic[Digital].from(myHList)}")

  /** so my intuitive idea is, to make change on each field/element in the HList */
  def convertJob: String => String = whateverItWas => "paas development"
  def keepInt: Int => Int                 = identity

  val myMappedHList = keepInt(myHList.head) :: convertJob(myHList.tail.head) :: myHList.tail.tail // Or just HNil

  println(
    s"\ncan we get a valid Platform instance? let's see: ${Generic[Platform].from(myMappedHList).getClass.getCanonicalName}"
  )
  /** yeah we are done, thank you! LOL */
}
