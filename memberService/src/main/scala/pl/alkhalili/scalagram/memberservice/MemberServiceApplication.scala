package pl.alkhalili.scalagram.memberservice

import cats.effect.{ExitCode, IO, IOApp}

import pl.alkhalili.scalagram.common.Microservice

import scala.concurrent.ExecutionContext

object MemberServiceApplication extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Microservice[IO]("memberservice",
                     8080,
                     new MemberService,
                     ExecutionContext.global).stream.compile.lastOrError
}
