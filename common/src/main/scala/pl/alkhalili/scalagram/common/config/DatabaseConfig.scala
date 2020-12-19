package pl.alkhalili.scalagram.common.config

import cats.effect.{Blocker, ConcurrentEffect, ContextShift, Resource}
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

case class Driver(driverClass: String) extends AnyVal {

  /**
    * Get the connection string based on driver type
    * @return a connection string
    */
  def connectionString(databaseConfig: DatabaseConfig): String = driverClass match {
    // FIXME BTW this is a little ugly. Will fix this
    case Driver.H2.driverClass =>
      s"jdbc:h2:mem:${databaseConfig.databaseName};DB_CLOSE_DELAY=-1"
  }
}

object Driver {
  val H2: Driver = Driver("org.h2.Driver")
}

case class DatabaseConfig(
    driver: String,
    host: String,
    user: String,
    password: String,
    port: Int,
    databaseName: String
) {
  def default = DatabaseConfig(Driver.H2.driverClass, "", "", "", 0, "default")
}

object DatabaseProvider {
  def mkTransactor[F[_]](databaseConfig: DatabaseConfig, xc: ExecutionContext, blocker: Blocker)(
      implicit csf: ContextShift[F],
      ce: ConcurrentEffect[F]
  ): Resource[F, HikariTransactor[F]] = {
    val driverObject = Driver(databaseConfig.driver)
    HikariTransactor.newHikariTransactor[F](
      driverObject.driverClass,
      driverObject.connectionString(databaseConfig),
      databaseConfig.user,
      databaseConfig.password,
      xc,
      blocker
    )
  }
}
