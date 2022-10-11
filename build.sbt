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

//libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test"
//
////libraryDependencies += "org.scalamock" % "scalamockjs_sjs0.6_2.12" % "3.6.0"
//libraryDependencies += "org.scalatestplus" % "mockito-4-6_3" % "3.2.14.0"

libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.3.0-SNAP3" % "test"

libraryDependencies ++= Seq(
//  "org.scalatest" %% "scalatest_2.13" % "3.0.4" % "test" ,
  "org.scalamock" % "scalamock_sjs0.6_2.12" % "4.4.0" % "test"
)

//libraryDependencies += "org.scalamock" %% "scalamock" % "5.1.0" % "test"
//libraryDependencies += "org.scalatest" %% "scalatest" % "2.1." % "test"