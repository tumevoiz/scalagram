package pl.alkhalili.scalagram.common

import cats.data.Kleisli
import cats.effect.{Effect, Sync}
import cats.implicits._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Request, Response}

trait Service[F[_]] extends Http4sDsl[F] {
  def routes(implicit F: Sync[F]): HttpRoutes[F]

  def mkRoutes(implicit F: Effect[F]): Kleisli[F, Request[F], Response[F]] =
    (routes <+> additionalRoutes).orNotFound

  def additionalRoutes(implicit F: Effect[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "healthz" => Ok("OK")
    }
}

object Service {
  case object AuthService
  case object MemberService
}
