import Dependencies._

lazy val patternsAkka = (project in file("patterns-akka")).
  settings(Settings.settings: _*).
  settings(Settings.coreSettings: _*).
  settings(libraryDependencies ++= patternAkkaDependencies)

lazy val tests = (project in file("tests")).
  settings(Settings.settings: _*).
  settings(Settings.testsSettings: _*).
  settings(libraryDependencies ++= testsDependencies)

lazy val core1 = (project in file("core")).
  settings(Settings.settings: _*).
  settings(Settings.core1Settings: _*).
  dependsOn(patternsAkka, tests).
  configs(Test)
