package pl.alkhalili.scalagram.common

import pl.alkhalili.scalagram.common.config.{DatabaseConfig, MicroserviceConfig}

case class Resources(serviceConfig: MicroserviceConfig, databaseConfig: DatabaseConfig)
