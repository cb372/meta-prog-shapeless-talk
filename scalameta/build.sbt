scalaVersion in ThisBuild := "2.11.8"

resolvers += Resolver.bintrayIvyRepo("scalameta", "maven")
addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0.132" cross CrossVersion.full)
scalacOptions += "-Xplugin-require:macroparadise"
scalacOptions in (Compile, console) := Seq()

libraryDependencies ++= Seq(
  "org.scalameta" %% "scalameta" % "1.4.0.544",
  "com.github.jknack" % "handlebars" % "4.0.6" % Test,
  "com.chuusai" %% "shapeless" % "2.3.2" % Test
)
