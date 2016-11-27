package foo

import com.github.jknack.handlebars._

object MustacheExample extends App with Util {

  @Mustache("/Users/chris/code/meta-prog-shapeless-talk/scalameta/template.txt")
  object NameAndWeather

  val nameAndWeather = NameAndWeather(firstName = "Chris", weather = "sunny")

  val context = caseClassToJavaMap(nameAndWeather)
  val renderer = new Handlebars().compileInline(NameAndWeather.template)
  println()
  println(renderer.apply(context))

}
