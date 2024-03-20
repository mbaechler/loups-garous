package loupgarou

import scala.util.Random

object syntax:
  extension [A](a: A) def |>[B](f: A => B): B = f(a)

  def tireAuHasard[T](choses: Set[T]): T =
    Random.shuffle(choses).head
      
  def log[T](step: String)(t: T) =
    println(s"$step $t")
    t

