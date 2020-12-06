package pl.alkhalili.scalagram.common

import pl.alkhalili.scalagram.common.config.{ApplicationConfig, DatabaseConfig, ServiceConfig}

case class Resources[F[_]](applicationConfig: ApplicationConfig,
                           repository: Repository[F, _ <: Entity])
