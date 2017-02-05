package foo

object CaseClassToCaseClassExample extends App {

  case class Input(foo: Int, baz: String)
  case class Output(foo: Int, bar: Double, baz: String)
  val in = Input(123, "wow")

  import CaseClassToCaseClass._

  println("Using macro:")
  println(Macros.caseClassToCaseClass[Input, Output](in, "bar" -> 4.56))

  println("Using shapeless:")
  import shapeless._
  import record._
  import syntax.singleton._
  val extraFields = ('bar ->> 4.56) :: HNil
  type Extra = Record.`'bar -> Double`.T
  val transform = Shapeless.Transform[Input, Output, Extra]
  println(transform(in, extraFields))

}
