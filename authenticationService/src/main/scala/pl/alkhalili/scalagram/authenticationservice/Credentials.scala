package pl.alkhalili.scalagram.authenticationservice

import pl.alkhalili.scalagram.common.Entity

case class Credentials(id: Long, userId: Long, password: String) extends Entity
