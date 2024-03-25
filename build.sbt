
ThisBuild / scalaVersion     := "3.4.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalacOptions     ++= Seq(
    "-encoding",
    "UTF-8",
  )
)

lazy val root = project.in(file("."))
  .settings(moduleName := "loup-garou")
  .settings(baseSettings: _*)
  .aggregate(core, slides)
  .dependsOn(core, slides)


lazy val core = project
  .settings(moduleName := "core")
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.21",
      "dev.zio" %% "zio-test" % "2.0.21" % Test,
      "org.typelevel" %% "cats-core" % "2.10.0" % Test,
      "dev.zio" %% "zio-interop-cats" % "23.1.0.0"
    )
  )
  .settings(baseSettings: _*)

lazy val slides = project
  .dependsOn(core)
  .settings(moduleName := "slides")
  .settings(baseSettings: _*)
  .settings(
    mdocIn := baseDirectory.value / "mdoc",
    mdocOut := baseDirectory.value / "docs",
  )
  .enablePlugins(MdocPlugin)