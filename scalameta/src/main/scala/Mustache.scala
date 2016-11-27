package foo

import scala.meta._
import scala.annotation.StaticAnnotation

class Mustache(templateFile: String) extends StaticAnnotation {

  inline def apply(annottees: Any): Any = meta {

    // Read a Mustache template from a file
    def loadTemplate(filename: String): String = {
      import java.nio.file._
      import java.nio.charset.StandardCharsets
      import scala.util.control.NonFatal
      try {
        new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8)
      } catch {
        case NonFatal(e) => abort(s"Failed to read template body from $filename. Exception: $e")
      }
    }

    // Extract the template filename from the annotation's parameter
    val filename = this match {
      case q"new $_(${Lit(f: String)})" => f
      case _ => abort("You must provide the template filename")
    }

    annottees match {
      case q"object $name extends { ..$earlyInits } with ..$parentCtors { $param => ..$body }" =>
        val templateBody: String = loadTemplate(filename)
        val variableNames: Set[String] = TemplateParser.findVariableNames(templateBody)
        val ctorParams = variableNames.to[collection.immutable.Seq].sorted.map(name => param"${Term.Name(name)}: String")
        q"""
          {
            object $name extends { ..$earlyInits } with ..$parentCtors { $param =>
              val template: String = $templateBody
              ..$body
            }
            case class ${Type.Name(name.toString)}(..$ctorParams)
          }
        """

      case _ => abort("You must annotate an object definition without an accompanying class.")
    }

  }

}
