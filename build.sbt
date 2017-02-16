val orgName = "io.github.daviddenton"

val projectName = "finagle-aws"

organization := orgName

name := projectName

description := "finagle aws"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.12.1", "2.11.8")

scalacOptions += "-deprecation"

scalacOptions += "-feature"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "com.twitter" %% "finagle-http" % "6.42.0" % "provided",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test")

licenses +=("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

pomExtra :=
  <url>http://github.com/daviddenton/finagle-aws</url>
    <scm>
      <url>git@github.com:daviddenton/{projectName}.scala.git</url>
      <connection>scm:git:git@github.com:daviddenton/{projectName}.scala.git</connection>
      <developerConnection>scm:git:git@github.com:daviddenton/{projectName}.scala.git</developerConnection>
    </scm>
    <developers>
      <developer>
        <name>David Denton</name>
        <email>dev@fintrospect.io</email>
        <organization>{projectName}</organization>
        <organizationUrl>http://daviddenton.github.io</organizationUrl>
      </developer>
    </developers>

bintrayOrganization := Some(orgName)

credentials += Credentials(Path.userHome / ".sonatype" / ".credentials")

