package foo

object CaseClassToCaseClassExample extends App {

  case class Input(a: Int, b: String)
  case class Output(a: Int, b: String, c: Boolean)
  val in = Input(123, "wow")

  import CaseClassToCaseClass._

  println("Using macro:")
  println(Macros.caseClassToCaseClass[Input, Output](in, "c" -> true))

  println("Using shapeless:")
  import shapeless._
  import record._
  import syntax.singleton._
  val extraFields = ('c ->> true) :: HNil
  type Extra = Record.`'c -> Boolean`.T
  val transform = Shapeless.Transform[Input, Output, Extra]
  println(transform(in, extraFields))

}
