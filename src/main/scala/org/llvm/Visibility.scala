package org.llvm

object Visibility extends Enumeration {
  type Visibility = Value

  val Default = Value(0)

  val Hidden = Value(1)

  val Protected = Value(2)
}
