package pl.alkhalili.scalagram.common

import cats.effect.{ConcurrentEffect, ExitCode, Timer}
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

case class Microservice[F[_]: ConcurrentEffect: Timer](
    name: String,
    port: Int,
    service: Service[F],
    xc: ExecutionContext
) {
  def stream: fs2.Stream[F, ExitCode] =
    for {
      exitCode <- BlazeServerBuilder[F](xc)
        .bindHttp(port, "0.0.0.0")
        .withHttpApp(service.mkRoutes)
        .serve
    } yield exitCode
}
