import sbt._

object Dependencies {

  lazy val version = new {
    val scalaTest = "3.0.1"
    val scalaCheck = "1.13.5"
    val akka = "2.5.3"
    val akkaHttp = "10.0.9"
    val scalaAsync = "0.9.6"
  }

  lazy val library = new {
    val test = "org.scalatest" %% "scalatest" % version.scalaTest % Test
    val check = "org.scalacheck" %% "scalacheck" % version.scalaCheck % Test


    // akka actors
    val akkaActor = Seq(
      "com.typesafe.akka" %% "akka-actor" % version.akka,
      "com.typesafe.akka" %% "akka-testkit" % version.akka % Test
    )

    // akka streams
    val akkaStream = Seq(
      "com.typesafe.akka" %% "akka-stream" % version.akka,
      "com.typesafe.akka" %% "akka-stream-testkit" % version.akka % Test
    )

    // akka http
    val akkaHttp = Seq(
      "com.typesafe.akka" %% "akka-http" % version.akkaHttp,
      "com.typesafe.akka" %% "akka-http-testkit" % version.akkaHttp % Test
    )

    val scalaAsync = "org.scala-lang.modules" %% "scala-async" % version.scalaAsync

    // finagle
    val finagle = Seq()
  }

  val coreDeps: Seq[ModuleID] = Seq(
    library.test,
    library.check
  )

  val patternAkkaDeps: Seq[ModuleID] = Seq(
    library.test,
    library.check
  ) ++ library.akkaActor

  val testsDeps: Seq[ModuleID] = Seq(
    library.test,
    library.check
  )

  val finagleDeps: Seq[ModuleID] = Seq(
    library.test,
    library.check
  )

  val coreDeps: Seq[ModuleID] = Seq(
    library.test,
    library.check,
    library.scalaAsync
  )
}
