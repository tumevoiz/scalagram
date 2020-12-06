package pl.alkhalili.scalagram.common.config

case class ServiceConfig(host: String, port: Int)

object ServiceConfig {
  val default: ServiceConfig = ServiceConfig("0.0.0.0", 8080)
}
