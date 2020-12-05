package pl.alkhalili.scalagram.common

import cats.effect.Effect
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway

object Migration {
  def doIt[F[_]](transactor: HikariTransactor[F])(implicit F: Effect[F]): F[Unit] =
    transactor.configure(dataSource =>
      F.pure {
        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()
        ()
    })
}
