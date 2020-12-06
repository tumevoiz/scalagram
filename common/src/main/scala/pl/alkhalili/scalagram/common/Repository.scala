package pl.alkhalili.scalagram.common

trait Repository[F[_], A <: Entity] {
  def all: fs2.Stream[F, A]
  def findById(id: Long): F[A]
  def update(id: Long, entity: A): F[Boolean]
  def insert(entity: A): F[A]
}
