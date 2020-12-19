package pl.alkhalili.scalagram.authenticationservice

import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import doobie.ExecutionContexts
import org.http4s.client.blaze.BlazeClientBuilder
import pl.alkhalili.scalagram.common.config.{ApplicationConfig, DatabaseProvider}
import pl.alkhalili.scalagram.common.{Microservice, Migration, Resources}
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax._
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext

object AuthenticationServiceApplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    loadResources.use(
      cfg =>
        Microservice[IO](
          new AuthenticationService(cfg.repository, cfg.httpClient),
          ExecutionContext.global
        ).stream(cfg.applicationConfig.serviceConfig)
    )

  // TODO export it to common
  private def loadResources: Resource[IO, Resources[IO]] =
    for {
      blocker    <- Blocker[IO]
      xc         <- ExecutionContexts.fixedThreadPool[IO](32)
      httpClient <- BlazeClientBuilder[IO](ExecutionContext.global).resource
      appConfig  <- Resource.liftF(ConfigSource.default.loadF[IO, ApplicationConfig](blocker))
      transactor <- DatabaseProvider.mkTransactor(appConfig.databaseConfig, xc, blocker)
      _          <- Resource.liftF(Migration.doIt(transactor))
      repository = new CredentialsRepository[IO](transactor)
    } yield Resources(appConfig, repository, httpClient)
}
