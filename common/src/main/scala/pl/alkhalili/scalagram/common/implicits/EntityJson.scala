package pl.alkhalili.scalagram.common.implicits

import io.circe.Json

trait EntityJson {
  implicit class EntityJsonHelpers(json: Json) {
    def withoutId: Json = {
      json.hcursor.downField("id").delete.top match {
        case Some(jsonWithoutId) => jsonWithoutId
        case None                => json
      }
    }
  }

  implicit class MemberJsonPrivacyModifier(json: Json) {
    def withoutEmail: Json = {
      json.hcursor.downField("email").delete.top match {
        case Some(jsonWithoutId) => jsonWithoutId
        case None                => json
      }
    }
  }
}
