package pl.alkhalili.scalagram.common

import org.http4s.client.Client
import pl.alkhalili.scalagram.common.config.{ApplicationConfig, DatabaseConfig, ServiceConfig}

case class Resources[F[_]](
    applicationConfig: ApplicationConfig,
    repository: Repository[F, _ <: Entity],
    httpClient: Client[F]
)
