Sample code for my talk [Meta-program and/or shapeless all the things!](https://slides.com/cb372/metaprog-shapeless-lsug-2016)

There are 4 separate sbt projects in the repo.

In each case, it probably makes sense to look in the test folder first.

## macros

* `echo` - a "hello world" def macro that does nothing except print out its input during compilation
* `optimise` - a def macro that rewrites `for (i <- 1 to n) { doStuff() }` comprehensions into `while` loops

There is also a JMH benchmark for the `optimise` macro.

## macro-annotation

A macro annotation for generating case classes from Mustache templates.

## scalameta

A port of the macro annotation to meta paradise.

## shapeless

A couple of example macros-vs-shapeless comparisons.
