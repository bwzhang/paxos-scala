name := "paxos"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" % "akka-cluster-metrics_2.12" % akkaVersion,
  "com.github.scopt" %% "scopt" % "3.6.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
