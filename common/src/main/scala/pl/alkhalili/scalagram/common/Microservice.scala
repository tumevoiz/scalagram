package pl.alkhalili.scalagram.common

import cats.effect.{ConcurrentEffect, ExitCode, Timer}
import cats.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import pl.alkhalili.scalagram.common.config.ServiceConfig
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext

case class Microservice[F[_]: ConcurrentEffect: Timer](
    service: Service[F],
    xc: ExecutionContext
) {
  def stream(serviceConfig: ServiceConfig): F[ExitCode] =
    for {
      exitCode <- BlazeServerBuilder[F](xc)
        .bindHttp(serviceConfig.port, serviceConfig.host)
        .withHttpApp(service.mkRoutes)
        .serve
        .compile
        .lastOrError
    } yield exitCode
}
