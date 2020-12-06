package pl.alkhalili.scalagram.memberservice

import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import doobie.ExecutionContexts
import pl.alkhalili.scalagram.common.config.{ApplicationConfig, DatabaseProvider}
import pl.alkhalili.scalagram.common.{Microservice, Migration, Resources}
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig.module.catseffect.syntax._

import scala.concurrent.ExecutionContext

object MemberServiceApplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    loadResources.use(
      cfg =>
        Microservice[IO](new MemberService(cfg.repository), ExecutionContext.global)
          .stream(cfg.applicationConfig.serviceConfig))

  // TODO export it to common
  private def loadResources: Resource[IO, Resources[IO]] =
    for {
      blocker    <- Blocker[IO]
      xc         <- ExecutionContexts.fixedThreadPool[IO](32)
      appConfig  <- Resource.liftF(ConfigSource.default.loadF[IO, ApplicationConfig](blocker))
      transactor <- DatabaseProvider.mkTransactor(appConfig.databaseConfig, xc, blocker)
      _          <- Resource.liftF(Migration.doIt(transactor))
      repository = new MemberRepository[IO](transactor)
    } yield Resources(appConfig, repository)
}
