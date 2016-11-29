package foo


object CaseClassToMapExample extends App {

  case class Foo(a: Int, b: String)
  val instance = Foo(123, "wow")

  println("Using macro:")
  println(CaseClassToMap.Macros.caseClassToMap(instance))

  println("Using shapeless:")
  println(CaseClassToMap.Shapeless.caseClassToMap(instance))

}
