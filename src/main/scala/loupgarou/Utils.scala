package loupgarou

extension [A](a: A) def |>[B](f: A => B): B = f(a)
