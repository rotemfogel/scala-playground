name := "scala-playground"

version := "0.1"

scalaVersion := "2.12.15"

resolvers ++= Seq(
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "postgres" at "https://postgres-maven-repository.s3-website-us-east-1.amazonaws.com/release",
  "confluent" at "https://packages.confluent.io/maven"
)

scalacOptions ++= Seq(
  "-deprecation", // Warning and location for usages of deprecated APIs.
  "-encoding", "utf-8", // Specify character encoding used by source files.
  "-explaintypes", // Explain type errors in more detail.
  "-feature", // For features that should be imported explicitly.
  "-unchecked", // Generated code depends on assumptions.
  "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
  "-Xlint:adapted-args", // An argument list is modified to match the receiver.
  "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
  "-Xlint:constant", // Constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:doc-detached", // A detached Scaladoc comment.
  "-Xlint:inaccessible", // Inaccessible types in method signatures.
  "-Xlint:infer-any", // A type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Xlint:unsound-match", // Pattern match may not be typesafe.
  "-Yno-adapted-args", // Do not autotuple.
  "-Ypartial-unification", // Enable partial unification in type constructor inference
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-extra-implicit", // More than one implicit parameter section is defined.
  "-Ywarn-inaccessible", // Inaccessible types in method signatures.
  "-Ywarn-infer-any", // A type argument is inferred to be `Any`.
  "-Ywarn-nullary-override", // non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-nullary-unit", // nullary method returns Unit.
  // "-Ywarn-numeric-widen",  // Numerics are implicitly widened.
  "-Ywarn-unused:implicits", // An implicit parameter is unused.
  "-Ywarn-unused:imports", // An import selector is not referenced.
  "-Ywarn-unused:locals", // A local definition is unused.
  "-Ywarn-unused:params", // A value parameter is unused.
  // "-Ywarn-unused:patvars",   // A variable bound in a pattern is unused.
  // "-Ywarn-value-discard",   // Non-Unit expression results are unused.
  "-Ywarn-unused:privates", // A private member is unused.
  "-Xfatal-warnings"
)

// @formatter:off
libraryDependencies ++= {
  val scalaTestVersion = "3.0.4"
  val spec2Version     = "3.9.5"
  val akkaVersion      = "2.6.17"
  val akkaHttpVersion  = "10.2.6"
  val json4sVersion    = "3.6.9"
  val awsVersion       = "1.12.91"
  val kafkaVersion     = "3.0.0"
  val circeVersion     = "0.13.0"
  val zioVersion       = "1.0.12"

  Seq(
    "org.slf4j"                 % "slf4j-api"             % "1.7.30",
    "ch.qos.logback"            % "logback-classic"       % "1.2.3",
    "com.typesafe.akka"        %% "akka-slf4j"            % akkaVersion,
    "com.typesafe.akka"        %% "akka-actor"            % akkaVersion,
    "com.typesafe.akka"        %% "akka-remote"           % akkaVersion,
    "com.typesafe.akka"        %% "akka-cluster"          % akkaVersion,
    "com.typesafe.akka"        %% "akka-actor-typed"      % akkaVersion,
    "com.typesafe.akka"        %% "akka-http"             % akkaHttpVersion,
    "org.json4s"               %% "json4s-native"         % json4sVersion,
    "io.circe"                 %% "circe-core"            % circeVersion,
    "io.circe"                 %% "circe-generic"         % circeVersion,
    "io.circe"                 %% "circe-parser"          % circeVersion,
    "com.typesafe.slick"       %% "slick-hikaricp"        % "3.2.3",
    "org.mariadb.jdbc"          % "mariadb-java-client"   % "2.4.1",
    "org.apache.commons"        % "commons-compress"      % "1.18",
    "com.github.luben"          % "zstd-jni"              % "1.3.7-3",
    "postgresql"                % "postgresql"            % "9.1-901-1.jdbc4",
    "com.amazonaws"             % "aws-java-sdk-s3"       % awsVersion,
    "com.amazonaws"             % "aws-java-sdk-ecr"      % awsVersion,
    "com.amazonaws"             % "aws-java-sdk-ecs"      % awsVersion,
    "com.amazonaws"             % "aws-java-sdk-athena"   % awsVersion,
    "com.amazonaws"             % "aws-java-sdk-glue"     % awsVersion,
    "org.apache.kafka"          % "kafka-clients"         % kafkaVersion,
    "org.bouncycastle"          % "bcpkix-jdk15on"        % "1.61",
    "dev.zio"                  %% "zio"                   % zioVersion,
    "dev.zio"                  %% "zio-streams"           % zioVersion,
    "com.typesafe.akka"        %% "akka-stream-kafka"     % "2.1.1",
    // "com.datastax.cassandra"    % "cassandra-driver-core" % "4.0.0",
    "com.datastax.oss"          % "java-driver-core"      % "4.13.0",
    "com.google.guava"          % "guava"                 % "28.1-jre",
    "com.google.code.gson"      % "gson"                  % "2.8.5",
    "org.antlr"                 % "antlr4-runtime"        % "4.7.2",
    "com.maxmind.db"            % "maxmind-db"            % "2.0.0",
    "org.scala-lang.modules"   %% "scala-xml"             % "1.2.0",
    "com.github.scopt"         %% "scopt"                 % "4.0.1",
    "org.typelevel"            %% "cats-effect"           % "3.1.1",
    "com.github.mpilquist"     %% "simulacrum"            % "0.19.0",
    // Test Dependencies
    "org.specs2"               %% "specs2-core"           % spec2Version     % Test,
    "org.scalatest"            %% "scalatest"             % scalaTestVersion % Test
  )
}
// @formatter:on

initialCommands in console +=
  """
import akka.actor._
import akka.cluster._
import akka.cluster.routing._
import akka.routing._
import com.typesafe.config._
import me.rotemfo.akka._
"""