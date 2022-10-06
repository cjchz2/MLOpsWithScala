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

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.10"
libraryDependencies += "com.opencsv" % "opencsv" % "5.7.0"
libraryDependencies += "com.nrinaudo" % "kantan.csv-generic_2.13" % "0.7.0"
