package pl.alkhalili.scalagram.authenticationservice

import cats.effect.Effect
import cats.implicits._
import doobie._
import doobie.implicits._
import pl.alkhalili.scalagram.common.Repository
import shapeless.HNil

class CredentialsRepository[F[_]: Effect](transactor: Transactor[F])
    extends Repository[F, Credentials] {
  override def all: fs2.Stream[F, Credentials] = ??? // Dont list all credentials

  def findByUserId(id: Long): F[Credentials] =
    sql"select id, user_id, password from credentials where user_id=$id"
      .query[Credentials]
      .unique
      .transact(transactor)

  override def update(id: Long, entity: Credentials): F[Boolean] =
    sql"update credentials set password=${entity.password} WHERE id=$id".update.run
      .transact(transactor)
      .map(_ > 1)

  override def insert(entity: Credentials): F[Credentials] =
    sql"insert into credentials(user_id, password) values(${entity.userId}, ${entity.password})".update
      .withUniqueGeneratedKeys[Long]("id")
      .transact(transactor)
      .map(id => entity.copy(id = id))

  override def findById(id: Long): F[Credentials] = ???
}
