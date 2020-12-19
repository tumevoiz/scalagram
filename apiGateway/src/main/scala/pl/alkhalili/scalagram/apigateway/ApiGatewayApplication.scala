package pl.alkhalili.scalagram.apigateway

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

class ApiGatewayApplication extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      exitCode <- BlazeServerBuilder[IO](ExecutionContext.global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(new ApiRouter[IO].mkRoutes)
        .serve
        .compile
        .lastOrError
    } yield exitCode
}
