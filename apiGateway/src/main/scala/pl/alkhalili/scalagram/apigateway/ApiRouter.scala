package pl.alkhalili.scalagram.apigateway

import cats.data.Kleisli
import cats.effect.{Effect, Sync}
import cats.implicits._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Request, Response}

class ApiRouter[F[_]] extends Http4sDsl[F] {
  def routes(implicit F: Sync[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "api" => Ok("OK")
  }

  def additionalRoutes(implicit F: Effect[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "healthz" => Ok("OK")
    }

  def mkRoutes(implicit F: Effect[F]): Kleisli[F, Request[F], Response[F]] =
    (routes <+> additionalRoutes).orNotFound
}
