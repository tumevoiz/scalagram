import Build._

lazy val root = Project(id = "scalagram", base = file("."))
  .dependsOn(apiGateway, authenticationService, memberService)
  .aggregate()

lazy val common =
  Project(id = "common", base = file("common")).settings(allSettings: _*)

lazy val apiGateway =
  Project(id = "apiGateway", base = file("apiGateway"))
    .settings(allSettings: _*)
    .settings(libraryDependencies ++= Dependencies.BCrypt)
    .settings(
      mainClass in Compile := Some(
        "pl.alkhalili.scalagram.apigateway.ApiGatewayApplication"
      )
    )
    .settings(
      packageName in Docker := "api-gateway",
      version in Docker := "latest",
      NativePackagerKeys.dockerBaseImage := "openjdk:latest",
      NativePackagerKeys.dockerExposedPorts := Seq(8080),
      NativePackagerKeys.dockerExposedVolumes := Seq("/opt/docker/logs")
    )
    .dependsOn(common)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val authenticationService =
  Project(id = "authenticationService", base = file("authenticationService"))
    .settings(allSettings: _*)
    .settings(libraryDependencies ++= Dependencies.BCrypt)
    .settings(
      mainClass in Compile := Some(
        "pl.alkhalili.scalagram.authenticationservice.AuthenticationServiceApplication"
      )
    )
    .settings(
      packageName in Docker := "authentication-service",
      version in Docker := "latest",
      NativePackagerKeys.dockerBaseImage := "openjdk:latest",
      NativePackagerKeys.dockerExposedPorts := Seq(8080),
      NativePackagerKeys.dockerExposedVolumes := Seq("/opt/docker/logs")
    )
    .dependsOn(common)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val memberService = Project(id = "memberService", base = file("memberService"))
  .settings(allSettings: _*)
  .settings(
    mainClass in Compile := Some("pl.alkhalili.scalagram.memberservice.MemberServiceApplication")
  )
  .settings(
    packageName in Docker := "member-service",
    version in Docker := "latest",
    NativePackagerKeys.dockerBaseImage := "openjdk:latest",
    NativePackagerKeys.dockerExposedPorts := Seq(8080),
    NativePackagerKeys.dockerExposedVolumes := Seq("/opt/docker/logs")
  )
  .dependsOn(common)
  .enablePlugins(JavaAppPackaging, DockerPlugin)
