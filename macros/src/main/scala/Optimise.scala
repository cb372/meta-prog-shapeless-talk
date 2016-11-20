import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

import PrettyPrinting._

object Optimise {

  def optimise[A](block: A): A = macro optimise_impl[A]

  def optimise_impl[A](c: Context)(block: c.Tree): c.Tree = {
    import c.universe._

    ???
  }

}
