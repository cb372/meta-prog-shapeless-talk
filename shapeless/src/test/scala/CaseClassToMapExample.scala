package foo


object CaseClassToMapExample extends App {

  case class Foo(wow: String, yeah: Int)
  val instance = Foo("hello", 123)

  println("Using macro:")
  println(CaseClassToMap.Macros.caseClassToMap(instance))

  println("Using shapeless:")
  println(CaseClassToMap.Shapeless.caseClassToMap(instance))

}
