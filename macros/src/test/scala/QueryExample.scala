package foo

import Query._

object QueryExample extends App {

  case class User(name: String, age: Int)

  val q = query[User](_.name == "Chris")

  println()
  println(q.toSQL)
  println()

}
