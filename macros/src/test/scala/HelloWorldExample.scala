package foo

import Echo._

object HelloWorldExample extends App {

  val greeting = echo {
    val x = 1
    val y = 2
    val z = x + y
    s"Hello! Did you know that z = $z"
  }

  println(greeting)

}
