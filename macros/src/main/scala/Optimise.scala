package foo

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

import PrettyPrinting._

object Optimise {

  def optimise[A](block: A): A = macro optimise_impl[A]

  def optimise_impl[A](c: Context)(block: c.Tree): c.Tree = {
    import c.universe._

    val result = block match {
      case q"for ($_ <- $n to $m) { $doStuff }" =>
        val i: TermName = c.freshName(TermName("i"))
        q"""
        var $i = $n.toInt
        while ($i < $m) { 
          $i = $i + 1
          $doStuff 
        }
        """
      case _ =>
        c.abort(c.enclosingPosition, "Sorry, don't know how to optimise that!")
    }
    println(showCode(result))
    result
  }

}
