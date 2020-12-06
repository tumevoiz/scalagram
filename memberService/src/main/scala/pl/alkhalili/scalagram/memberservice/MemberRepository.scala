package pl.alkhalili.scalagram.memberservice

import cats.effect.Effect
import pl.alkhalili.scalagram.common.Repository
import doobie._
import doobie.implicits._
import cats.implicits._
import shapeless.HNil

class MemberRepository[F[_]: Effect](transactor: Transactor[F]) extends Repository[F, Member] {
  override def all: fs2.Stream[F, Member] =
    sql"select id, name from members".query[Member].stream.transact(transactor)

  override def findById(id: Long): F[Member] =
    sql"select id, name from members where id=$id"
      .query[Member]
      .unique
      .transact(transactor)

  def findByName(name: String): F[Option[Member]] = {
    Query[HNil, Member](s"select * from members where name='$name'")
      .toQuery0(HNil)
      .option
      .transact(transactor)
  }

  override def update(id: Long, entity: Member): F[Boolean] =
    sql"update members set name=${entity.name}, email=${entity.email} WHERE id=$id".update.run
      .transact(transactor)
      .map(_ > 1)

  override def insert(entity: Member): F[Member] =
    sql"insert into members(name, email) values(${entity.name}, ${entity.email})".update
      .withUniqueGeneratedKeys[Long]("id")
      .transact(transactor)
      .map(id => entity.copy(id = id))
}
