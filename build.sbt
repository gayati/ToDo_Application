name := """ToDo_App"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "com.typesafe.play" %% "play" % "2.5.0-M1"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"

libraryDependencies += specs2 % Test

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.23"

libraryDependencies += "org.webjars" % "bootstrap" % "3.1.1-2"

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"
