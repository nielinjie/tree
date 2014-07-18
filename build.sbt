name := "totemPoles"

version := "0.1"

scalaVersion := "2.10.3"

resolvers +="baidu" at "http://maven.duapp.com/nexus/content/repositories/releases/"

libraryDependencies ++= Seq(
    "net.databinder" %% "unfiltered"        % "0.7.1",
    "net.databinder" %% "unfiltered-jetty"  % "0.7.1",
    "net.databinder" %% "unfiltered-filter" % "0.7.1",
    "net.databinder" %% "unfiltered-util"   % "0.7.1",
    "net.databinder" %% "unfiltered-json4s"   % "0.7.1",
    "org.scalaz" %% "scalaz-core" % "7.0.3",
    "org.mongodb" %% "casbah" % "2.6.4",
    "org.json4s" %% "json4s-mongo" % "3.2.6",
    "org.json4s" %% "json4s-ext" % "3.2.6",
    "com.github.nscala-time" %% "nscala-time" % "0.6.0",
    "nielinjie" %% "util-data" % "1.0",
    "com.escalatesoft.subcut" %% "subcut" % "2.0",
    "net.databinder" %% "dispatch-http" % "0.8.9",
    "net.databinder" %% "dispatch-http-json" % "0.8.9",
    "com.baidu.bae" % "baev3-sdk" % "1.0.1",
    "org.slf4j" % "slf4j-log4j12" % "1.7.7",
    "net.databinder" %% "unfiltered-specs2" % "0.7.1" % "test",
    "com.github.athieriot" %% "specs2-embedmongo" % "0.6.0" % "test",
    "org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115" % "container",
    "org.eclipse.jetty" % "jetty-plus"   % "9.1.0.v20131115" % "container"
)

net.virtualvoid.sbt.graph.Plugin.graphSettings


seq(webSettings :_*)
