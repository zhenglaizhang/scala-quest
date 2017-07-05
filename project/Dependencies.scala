import sbt._

object Dependencies {

  lazy val version = new {
    val scalaTest = "3.0.1"
    val scalaCheck = "1.13.5"
  }

  lazy val library = new {
    val test = "org.scalatest" %% "scalatest" % version.scalaTest % Test
    val check = "org.scalacheck" %% "scalacheck" % version.scalaCheck % Test


    // akka actors
    val akkaActor = Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.3",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % Test
    )

    // akka streams
    val akkaStream = Seq(
      "com.typesafe.akka" %% "akka-stream" % "2.5.3",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.3" % Test
    )

    // akka http
    val akkaHttp = Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.9",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test
    )
  }

  val patternAkkaDependencies: Seq[ModuleID] = Seq(
    library.test,
    library.check
  ) ++ library.akkaActor

  val testsDependencies: Seq[ModuleID] = Seq(
    library.test,
    library.check
  )

  val core1Dependencies: Seq[ModuleID] = Seq(
    library.test,
    library.check
  )

}
