
scalaVersion := "2.13.2"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

crossPaths := false

organization := "com.m3.play2"

name := "play2-sentry"

version := "3.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "io.sentry" % "sentry-logback" % "1.7.5",
  "com.typesafe.play" % "play_2.13" % "2.8.2" % "provided"
)

publishTo := version { v: String =>
  sys.env.get("REPOSITORY_URL").map { base =>
    if (v.trim.endsWith("SNAPSHOT"))
      "snapshots" at base + "libs-snapshots"
    else
      "releases" at base + "libs-releases"
  }
}.value

resolvers += Resolver.typesafeRepo("releases")

