name := """todo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.h2database"  %  "h2"                           % "1.4.191",
  "org.scalikejdbc" %% "scalikejdbc"                  % "2.4.0",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "2.4.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.5.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.mockito" % "mockito-core" % "2.7.14" % Test
)

