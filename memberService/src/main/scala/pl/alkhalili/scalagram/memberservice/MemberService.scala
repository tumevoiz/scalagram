package pl.alkhalili.scalagram.memberservice

import cats.effect.Effect
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import pl.alkhalili.scalagram.common.{Repository, Service}
import org.http4s.circe.CirceEntityCodec._
import cats.implicits._
import pl.alkhalili.scalagram.common.implicits._

class MemberService[F[_]: Effect, R <: Repository[F, _]](_repository: R) extends Service[F] {
  private val repository = _repository.asInstanceOf[MemberRepository[F]]
  override def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "findByName" / name =>
      for {
        member   <- repository.findByName(name)
        response <- member.fold(NotFound())(m => Ok(m.asJson.withoutId.withoutEmail))
      } yield response
  }
}
