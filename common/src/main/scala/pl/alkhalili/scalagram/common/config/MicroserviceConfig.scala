package pl.alkhalili.scalagram.common.config

case class MicroserviceConfig(host: String, port: Int)

object MicroserviceConfig {
  val default: MicroserviceConfig = MicroserviceConfig("0.0.0.0", 8080)
}
