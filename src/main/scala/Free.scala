import cats.{Monad, ~>}
import cats.syntax.flatMap._

sealed trait Free[F[_], A] {
  import Free._

  def map[B](f: A => B): Free[F, B] = flatMap(a => Point[F, B](f(a)))

  def flatMap[B](f: A => Free[F, B]): Free[F, B] = this match {
    case Point(a) => f(a)
    case Join(fa, ff) => Join(fa, ((free: Free[F, A]) => free.flatMap(f)) compose ff)
  }

  def foldMap[G[_]](nt: F ~> G)(implicit G: Monad[G]): G[A] =
    this match {
      case Point(a) => G.point(a)
      case Join(fa, f) => nt(fa.asInstanceOf[F[A]]).flatMap(f(_).foldMap(nt))
    }
}

object Free {
  def point[F[_], A](a: A): Free[F, A] = Point(a)
  def liftF[F[_], A](fa: F[A]): Free[F, A] = Join(fa, point)

  case class Point[F[_], A](a: A) extends Free[F, A]
  case class Join[F[_], A, B](fa: F[A], f: A => Free[F, B]) extends Free[F, B]
}