package foo

object TemplateParser {

  // Note: this is an over-simplified view of Mustache syntax.
  // This parser would not correctly handle templates containing
  // partials, comments, etc.
  private val Placeholder = """\{\{ *([A-Za-z0-9_]+) *\}\}""".r

  def findVariableNames(template: String): Set[String] =
    Placeholder.findAllMatchIn(template).map(_.group(1)).toSet

  }
