Sample code for my talk [Meta-program and/or shapeless all the things!](https://slides.com/cb372/metaprog-shapeless-lsug-2016)

There are 4 separate sbt projects in the repo.

In each case, it probably makes sense to look in the test folder first.

## macros

* `echo` - a "hello world" def macro that does nothing except print out its input during compilation
* `optimise` - a def macro that rewrites `for (i <- 1 to n) { doStuff() }` comprehensions into `while` loops
* `query` - an example of using a def macro to lift code written in an internal DSL into another encoding, in this case in order to construct an SQL statement

There is also a JMH benchmark for the `optimise` macro.

### A note about the optimise example

This turned out to be a bad example, because anyone who sees it immediately wants to support code like this :

```
optimise {
  for (i <- 1 to 10) { println(i) }
}
```

But this is surprisingly difficult. Splicing the already-typechecked `println(i)` into the newly generated tree doesn't work, because the symbols for the old `i` and the new `i` don't match.

Wrapping the tree in `c.untypecheck(...)` is supposed to fix this, but it didn't work when I tried it.

## macro-annotation

A macro annotation for generating case classes from Mustache templates.

## scalameta

A port of the macro annotation to meta paradise.

## shapeless

A couple of example macros-vs-shapeless comparisons.
