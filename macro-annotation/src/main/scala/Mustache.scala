package foo

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context
import scala.annotation.StaticAnnotation


class Mustache(templateFile: String) extends StaticAnnotation {

  def macroTransform(annottees: Any*): Any = macro Mustache.fromTemplate_impl

}

object Mustache {

  def fromTemplate_impl(c: Context)(annottees: c.Tree*): c.Tree = {
    import c.universe._

    def fail(message: String) = c.abort(c.enclosingPosition, message)

    // Read a Mustache template from a file
    def loadTemplate(filename: String): String = {
      import java.nio.file._
      import java.nio.charset.StandardCharsets
      import scala.util.control.NonFatal
      try {
        new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8)
      } catch {
        case NonFatal(e) => fail(s"Failed to read template body from $filename. Exception: $e")
      }
    }

    // Extract the template filename from the annotation's parameter
    val filename = c.macroApplication match {
      case Apply(Select(Apply(_, List(Literal(Constant(f: String)))), _), _) => f
      case _ => fail("You must provide the template filename")
    }

    annottees match {
      case List(q"object $name extends $parent { ..$body }") =>
        val templateBody: String = loadTemplate(filename)
        val variableNames: Set[String] = TemplateParser.findVariableNames(templateBody)
        val ctorParams = variableNames.toSeq.sorted.map(name => q"val ${TermName(name)}: String")
        q"""
          object $name extends $parent {
            val template: String = $templateBody
            $body
          }
          case class ${TypeName(name.toString)}(..$ctorParams)
        """

      case _ => fail("You must annotate an object definition without an accompanying class.")
    }

  }

}
