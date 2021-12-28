package org.llvm.dwarf.encoding

object Visibility extends Enumeration {
  type Visibility = Value

  val local: Visibility = Value(0x01)
  val exported: Visibility = Value(0x02)
  val qualified: Visibility = Value(0x03)
}
