package foo

object CaseClassToMap {

  object Macros {
    import scala.language.experimental.macros
    import scala.reflect.macros.blackbox.Context
    
    def caseClassToMap[A](a: A): Map[String, Any] =
      macro impl[A]

    def impl[A: c.WeakTypeTag](c: Context)(a: c.Tree): c.Tree = {
      import c.universe._
      val A = weakTypeOf[A]
      if (!(A.typeSymbol.isClass && A.typeSymbol.asClass.isCaseClass))
        c.abort(c.enclosingPosition, "Sorry, case classes only")
      val primaryCtor = A.typeSymbol.asClass.primaryConstructor.asMethod
      val params = primaryCtor.paramLists.flatten
      val kvPairs = params.map { k =>
        q"${k.name.toString} -> $a.${k.name.toTermName}"
      }
      q"""_root_.scala.collection.immutable.Map($kvPairs: _*)"""
    }

  }

  object Shapeless {
    import shapeless._
    import shapeless.ops.hlist.ToList
    import shapeless.ops.record.Fields

    def caseClassToMap[A, L <: HList, F <: HList](a: A)
      (implicit 
        generic: LabelledGeneric.Aux[A, L],
        fields: Fields.Aux[L, F],
        toList: ToList[F, (Symbol, Any)]
      ): Map[String, Any] = {
      val labelledGen = generic.to(a)
      val fieldsHlist = fields(labelledGen)
      toList(fieldsHlist)
        .map { case (symbol, value) => (symbol.name, value) }
        .toMap
    }

  }

}
