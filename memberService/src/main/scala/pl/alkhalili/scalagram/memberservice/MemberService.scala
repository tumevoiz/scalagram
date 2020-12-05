package pl.alkhalili.scalagram.memberservice

import cats.effect.Effect
import org.http4s.HttpRoutes
import pl.alkhalili.scalagram.common.Service

class MemberService[F[_]: Effect] extends Service[F] {
  override def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "hello" => Ok("Hello world!")
  }
}
