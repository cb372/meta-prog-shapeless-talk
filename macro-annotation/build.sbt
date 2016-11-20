scalaVersion in ThisBuild := "2.12.0"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.github.jknack" % "handlebars" % "4.0.6" % Test,
  "com.chuusai" %% "shapeless" % "2.3.2" % Test
)
scalacOptions += "-deprecation"
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
