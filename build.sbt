name := "free"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions := List(
  "-Ypartial-unification",
  "-language:higherKinds"
)

libraryDependencies += "org.typelevel" %% "cats-core" % "1.5.0-RC1"

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")