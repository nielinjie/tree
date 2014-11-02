name := "totemPoles"

version := "0.1"

scalaVersion := "2.11.2"

resolvers += "baidu" at "http://maven.duapp.com/nexus/content/repositories/releases/"

libraryDependencies ++= Seq(
  "nielinjie" %% "util-cloud" % "1.0",
  "net.databinder" %% "unfiltered-specs2" % "0.8.+" % "test",
  "com.github.athieriot" %% "specs2-embedmongo" % "0.7.0" % "test"
)


jetty()
