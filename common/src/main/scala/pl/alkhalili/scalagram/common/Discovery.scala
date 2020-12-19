package pl.alkhalili.scalagram.common

import scala.util.Properties

object Discovery {
  def envoyEndpoint: Option[String] =
    Properties.envOrNone("ENVOY_ENDPOINT")

  def throughEnvoy(endpoint: String): Option[String] =
    envoyEndpoint.map(envoy => s"$envoy$endpoint")
}
