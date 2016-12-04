package foo

object CaseClassToCaseClass {

  object Macros {
    import scala.language.experimental.macros
    import scala.reflect.macros.blackbox.Context
    
    def caseClassToCaseClass[A, B](a: A, extraParams: (String, Any)*): B =
      macro Bundle.impl[A, B]

    class Bundle(val c: Context) {
      import c.universe._

      private case class TreeWithActualType(tree: Tree, actualType: Type)

      private def fail(msg: String) =
        c.abort(c.enclosingPosition, msg)

      def impl[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Tree, extraParams: c.Expr[Tuple2[String, Any]]*): c.Tree = {
        val A = weakTypeOf[A]
        val B = weakTypeOf[B]

        if (!(A.typeSymbol.isClass && A.typeSymbol.asClass.isCaseClass))
          fail("Sorry, case classes only")
        if (!(B.typeSymbol.isClass && B.typeSymbol.asClass.isCaseClass))
          fail("Sorry, case classes only")

        val inputPrimaryCtor = A.typeSymbol.asClass.primaryConstructor.asMethod
        val inputParams = inputPrimaryCtor.paramLists.flatten
        val inputParamsMap: Map[TermName, TreeWithActualType] = inputParams.map { k =>
          val termName = k.name.toTermName
          termName -> TreeWithActualType(q"$a.$termName", k.info)
        }.toMap

        val extraParamsMap: Map[TermName, TreeWithActualType] = extraParams.map { expr =>
          val (key, value) = expr.tree match {
            case q"scala.Predef.ArrowAssoc[$_]($k).->[$_]($v)" => (k, v)
            case q"($k, $v)" => (k, v)
            case other => fail("You must pass extra params as either key -> value or (key, value)")
          }
          val q"${keyAsString: String}" = key
          val keyName = TermName(keyAsString)
          val actualValueType = expr.actualType.typeArgs(1)
          keyName -> TreeWithActualType(value.asInstanceOf[Tree], actualValueType)
        }.toMap

        val allParams: Map[TermName, TreeWithActualType] = inputParamsMap ++ extraParamsMap

        val outputPrimaryCtor = B.typeSymbol.asClass.primaryConstructor.asMethod
        val paramLists: List[List[Tree]] = 
          for (ps <- outputPrimaryCtor.paramLists) yield {
            for (p <- ps) yield {
              val termName = p.name.toTermName
              allParams.get(termName) match {
                case Some(t) if t.actualType weak_<:< p.typeSignature => t.tree
                case Some(t) => fail(s"Parameter ${termName.toString} has wrong type. Expected ${p.typeSignature} but got ${t.actualType}")
                case None => fail(s"Missing parameter of type ${termName.toString}")
              }
            }
          }

        q"new ${B.typeSymbol}(...$paramLists)"
      }
    }

  }

  object Shapeless {
    import shapeless._
    import shapeless.ops.hlist._

    trait Transform[A, B, E <: HList] {
      def apply(a: A, extraFields: E): B
    }

    object Transform {
      // Credit: this is based on @davegurnell's "migrations" example:
      // https://github.com/underscoreio/shapeless-guide-code/blob/solutions/migrations/src/main/scala/migrations.scala

      implicit def genericTransform[
        A,
        B,
        ARepr         <: HList,
        BRepr         <: HList,
        CommonFields  <: HList,
        ExtraFields   <: HList,
        Unaligned     <: HList
      ](implicit
        aGen    : LabelledGeneric.Aux[A, ARepr],
        bGen    : LabelledGeneric.Aux[B, BRepr],
        inter   : Intersection.Aux[ARepr, BRepr, CommonFields],
        diff    : Diff.Aux[BRepr, CommonFields, ExtraFields],
        prepend : Prepend.Aux[ExtraFields, CommonFields, Unaligned],
        align   : Align[Unaligned, BRepr]
      ): Transform[A, B, ExtraFields] =
        new Transform[A, B, ExtraFields] {
          def apply(a: A, extra: ExtraFields): B = {
            val aRepr     = aGen.to(a)
            val common    = inter(aRepr)
            val unaligned = prepend(extra, common)
            val bRepr     = align(unaligned)
            bGen.from(bRepr)
          }
        }

      def apply[A, B, E <: HList](implicit t: Transform[A, B, E]) = t

    }

  }

}
