package foo

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

trait Query {

  def tableName: String

  def filter: (String, String)

  def toSQL: String = 
    s"SELECT * FROM $tableName WHERE ${filter._1} == '${filter._2}'"

}

object Query {

  def query[A](condition: A => Boolean): Query = macro query_impl[A]

  def query_impl[A: c.WeakTypeTag](c: Context)(condition: c.Tree): c.Tree = {
    import c.universe._

    val tableName = weakTypeOf[A].typeSymbol.name.toString.toLowerCase

    condition match {
      case q"""???""" =>
        q"""
        ???
        """
      case _ =>
        c.abort(c.enclosingPosition, "Sorry, I don't understand")
    }
  }

}
