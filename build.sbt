import Dependencies._

lazy val `akka-patterns` = (project in file("akka-patterns"))
  .settings(Settings.settings: _*)
  .settings(Settings.coreSettings: _*)
  .settings(libraryDependencies ++= patternAkkaDeps)

lazy val tests = (project in file("tests"))
  .settings(Settings.settings: _*)
  .settings(Settings.testsSettings: _*)
  .settings(libraryDependencies ++= testsDeps)

lazy val finagle = (project in file("finagle"))
  .settings(Settings.settings: _*)
  .settings(Settings.testsSettings: _*)
  .settings(libraryDependencies ++= finagleDeps)

lazy val core = (project in file("core"))
  .settings(Settings.settings: _*)
  .settings(Settings.core1Settings: _*)
  .settings(libraryDependencies ++= coreDeps)
  .dependsOn(`akka-patterns`, finagle, tests)
  .configs(Test)
