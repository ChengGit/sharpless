# Sharpless
knowledge sharing about [`shapeless`](https://github.com/milessabin/shapeless) with team.

The codebase is not properly sharpened, that's where the project name comes from. LOL

# What's in sharpless
This project is used to demostrate features of `shapeless` by solving this problem: 
 - converting instances of case class A into case class B where A and B has same amount of attributes

The whole solutions and some `shapeless` related topics are split into smaller chapters:
 - [chapter 0 - problem statement](src/main/scala/io/cheng/sharpless/problems/Chap0_Problem_Statement.scala)
 - [chapter 1 - concept: aux pattern](src/main/scala/io/cheng/sharpless/basics/Chap1_Concepts_Aux_Pattern.scala)
 - [chapter 2 - concept: product type](src/main/scala/io/cheng/sharpless/basics/Chap2_Concept_Product_Type.scala)
 - [chapter 3 - concept: coproduct type](src/main/scala/io/cheng/sharpless/basics/Chap3_Concept_Coproduct_Type.scala)
 - [chapter 4 - map over polymorphic function](src/main/scala/io/cheng/sharpless/demos/Chap4_Map_Over_Polymorphic_Function.scala)
 - [chapter 5 - typeclass based converter](src/main/scala/io/cheng/sharpless/demos/Chap5_Type_Class_Based_Converter.scala)
 
 # How to run it
 Each chapter is wrapped inside a scala single object which extends `App`, in other words, there are multiple main classes
 in the project. 
 There are some `println` inside each of object, in order to show how generated data looks like.
 To observe printed results, one can use `show discoveredMainClasses` inside sbt console, and `runMain <full_class_name>`
  one example can be: 
```sbt
sbt:sharpless> show discoveredMainClasses
[info] * io.cheng.sharpless.basics.Chap1_Concepts_Aux_Pattern
[info] * io.cheng.sharpless.basics.Chap2_Concept_Product_Type
[info] * io.cheng.sharpless.basics.Chap3_Concept_Coproduct_Type
[info] * io.cheng.sharpless.demos.Chap0_Problem_Statement
[info] * io.cheng.sharpless.demos.Chap4_Map_Over_Polymorphic_Function
[info] * io.cheng.sharpless.demos.Chap5_Type_Class_Based_Converter
[success] Total time: 0 s, completed 07-May-2019 17:50:01

sbt:sharpless> runMain io.cheng.sharpless.demos.Chap4_Map_Over_Polymorphic_Function
[warn] Multiple main classes detected.  Run `show discoveredMainClasses` to see the list
[info] Running io.cheng.sharpless.demos.Chap4_Map_Over_Polymorphic_Function
printing the platform instance converted by Poly Function: Platform(1,paas development)
printing the platform instance converted by type class `PolyConverter` Platform(1,paas development)
[success] Total time: 1 s, completed 07-May-2019 17:50:05
 ```
   
 # Limitations
 this project is in a very early stage so refactorings and re-structuring will be expected as on-going process.
 
 # Todo list
 - [ ] Align
 - [ ] LabelledGeneric
 - [ ] Selector
