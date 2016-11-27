package foo

import shapeless._
import shapeless.ops.record._
import scala.collection.JavaConverters._

trait Util {
  import MustacheExample.NameAndWeather

  def caseClassToJavaMap(cc: NameAndWeather): java.util.Map[String, String] = {
    val generic = LabelledGeneric[NameAndWeather]
    val fieldsHlist = Fields[generic.Repr].apply(generic.to(cc))
    val fieldsList = fieldsHlist.toList[(Symbol, String)]
    val map: Map[String, String] = fieldsList.map {
      case (sym, value) => (sym.name, value)
    }.toMap
    map.asJava
  }

}
