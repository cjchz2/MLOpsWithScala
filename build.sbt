ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "MLOpsFromScratch"
  )

// Use https://search.maven.org/search?q=g:commons-math for dependency
libraryDependencies ++= Seq(
    "org.apache.commons" % "commons-math3" % "3.3"
)


libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.3"
)
