package pl.alkhalili.scalagram.common.implicits

import io.circe.Json

// TODO: Remove it
// Not used anymore due to architecture change
trait JsonOps {
  implicit class EntityOps(json: Json) {
    def withoutId: Json = {
      json.hcursor.downField("id").delete.top match {
        case Some(jsonWithoutId) => jsonWithoutId
        case None                => json
      }
    }
  }

  implicit class MemberOps(json: Json) {
    def withoutEmail: Json = {
      json.hcursor.downField("email").delete.top match {
        case Some(jsonWithoutId) => jsonWithoutId
        case None                => json
      }
    }
  }
}
