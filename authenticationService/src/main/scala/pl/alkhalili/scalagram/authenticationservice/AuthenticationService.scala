package pl.alkhalili.scalagram.authenticationservice

import cats.effect.Effect
import cats.effect._
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{HttpRoutes, Response}
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.Client
import pl.alkhalili.scalagram.common.implicits._
import pl.alkhalili.scalagram.common.{Discovery, Repository, Service}
import org.http4s.circe.CirceEntityCodec._
import org.http4s.Status

class AuthenticationService[F[_]: Effect, R <: Repository[F, _]](
    _repository: R,
    httpClient: Client[F]
) extends Service[F] {
  private val repository = _repository.asInstanceOf[CredentialsRepository[F]]

  import com.github.t3hnar.bcrypt._

  def routes(implicit F: Sync[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "login" =>
      for {
        loginRequest  <- req.as[LoginRequest]
        foundMember   <- findMember(loginRequest)
        credentials   <- repository.findByUserId(foundMember.id)
        authenticated <- authenticate(loginRequest, credentials)
      } yield authenticated
    case req @ POST -> Root / "createCredentials" =>
      for {
        credentials       <- req.as[Credentials]
        encryptedPassword <- F.pure(credentials.password.bcrypt)
        encryptedCredentials = credentials.copy(id = credentials.id, password = encryptedPassword)
        _        <- repository.insert(encryptedCredentials)
        response <- Created()
      } yield response
  }

  private def findMember(loginRequest: LoginRequest): F[Member] = {
    val endpoint = Discovery.throughEnvoy(s"/member/${loginRequest.username}")
    httpClient.expect[Member](endpoint.get)
  }

  private def authenticate(loginRequest: LoginRequest, credentials: Credentials)(
      implicit F: Sync[F]
  ): F[Response[F]] = {
    val isCorrect = for {
      encryptedPassword <- loginRequest.password.bcryptSafe(21).toOption
      passwordCorrect   <- credentials.password.isBcryptedSafe(encryptedPassword).toOption
    } yield passwordCorrect

    isCorrect match {
      case Some(_) => Ok("TOKEN")
      case None    => F.pure(Response(status = Status.Unauthorized))
    }
  }
}
