name := "sharpless"

version := "0.1"

scalaVersion := "2.12.8"


val V = new {
  val shapeless     = "2.3.3"
}

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-explaintypes",
  "-Yrangepos",
  "-feature",
  "-Xfuture",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:existentials",
  "-unchecked",
  "-Yno-adapted-args",
  "-Xlint:_,-type-parameter-shadow",
  "-Xsource:2.13",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  //"-Xfatal-warnings",
  "-opt-warnings",
  "-Ywarn-extra-implicit",
//  "-Ywarn-unused:_,imports",
//  "-Ywarn-unused:imports",
  "-opt:l:inline",
  "-opt-inline-from:<source>"
)

libraryDependencies ++= Seq("com.chuusai"                %% "shapeless"                        % V.shapeless)