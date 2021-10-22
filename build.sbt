lazy val root = (project in file("."))
  .settings(
    organization := "com.m3.play2",
    name := "play2-sentry",
    scalaVersion := "2.13.10",
      libraryDependencies ++= Seq(
      "io.sentry" % "sentry-logback" % "1.7.30",
      "com.typesafe.play" %% "play" % "2.8.19" % Provided
    )
  )
  .settings(
    homepage := Some(url("https://github.com/m3dev/play2-sentry")),
    licenses := Seq(
      "The MIT License" -> url("https://opensource.org/licenses/mit-license.php")
    ),
    developers := List(
      Developer(
        "kijuky",
        "Kizuki YASUE",
        "kizuki-yasue@m3.com",
        url("https://github.com/kijuky")
      )
    )
  )