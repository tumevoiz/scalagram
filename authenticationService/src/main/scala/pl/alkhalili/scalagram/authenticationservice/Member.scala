package pl.alkhalili.scalagram.authenticationservice

import pl.alkhalili.scalagram.common.Entity

case class Member(id: Long, name: String, email: String) extends Entity
