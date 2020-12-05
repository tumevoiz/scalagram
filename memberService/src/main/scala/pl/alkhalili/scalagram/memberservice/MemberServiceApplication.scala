package pl.alkhalili.scalagram.memberservice

import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import doobie.ExecutionContexts
import pl.alkhalili.scalagram.common.config.ApplicationConfig
import pl.alkhalili.scalagram.common.{Microservice, Migration}
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig.module.catseffect.syntax._

import scala.concurrent.ExecutionContext

object MemberServiceApplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    loadResources.use(
      cfg =>
        Microservice[IO](new MemberService, ExecutionContext.global)
          .stream(cfg.microserviceConfig))

  // TODO export it to common
  private def loadResources: Resource[IO, ApplicationConfig] =
    for {
      blocker    <- Blocker[IO]
      xc         <- ExecutionContexts.fixedThreadPool[IO](32)
      config     <- loadConfig
      transactor <- config.databaseConfig.transactor(xc, blocker)
      _          <- Resource.liftF(Migration.doIt(transactor))
    } yield config

  private def loadConfig: Resource[IO, ApplicationConfig] =
    for {
      blocker       <- Blocker[IO]
      serviceConfig <- Resource.liftF(ConfigSource.default.loadF[IO, ApplicationConfig](blocker))
    } yield serviceConfig
}
