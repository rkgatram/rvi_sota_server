logLevel := Level.Warn

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Flyway" at "https://flywaydb.org/repo"
)

addSbtPlugin("com.typesafe.play" %% "sbt-plugin" % "2.5.3")

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.4.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "3.2.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.2.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")

addSbtPlugin("com.github.ddispaltro" % "sbt-reactjs" % "0.5.2")
