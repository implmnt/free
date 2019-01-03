import cats.{Id, Monoid, ~>}
import cats.instances.int._

object Application extends App {

  sealed trait Algebra[A]

  object Algebra {
    case class Concat[A](x: A, y: A)(implicit val monoid: Monoid[A]) extends Algebra[A]

    def interpret[A](algebra: Algebra[A]): A =
      algebra match {
        case c @ Concat(x, y) => c.monoid.combine(x, y)
      }
  }

  type FreeAlgebra[A] = Free[Algebra, A]

  def concat[A : Monoid](x: A, y: A): FreeAlgebra[A] = Free.liftF(Algebra.Concat(x, y))

  object algebraToId extends (Algebra ~> Id) {
    def apply[A](fa: Algebra[A]): Id[A] = Algebra.interpret(fa)
  }

  val program =
    for {
      x <- concat(1, 2)
      y <- concat(x, 10)
      z <- concat(y, 17)
    } yield z

  println(s"res ${program.foldMap(algebraToId)}")

}
