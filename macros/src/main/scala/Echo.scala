import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

import PrettyPrinting._

object Echo {

  def echo[A](block: A): A = macro echo_impl[A]

  def echo_impl[A](c: Context)(block: c.Tree): c.Tree = {
    import c.universe._

    ???
  }

}
