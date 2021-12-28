package org.llvm.dwarf.encoding

object Virtuality extends Enumeration {
  type Virtuality = Value

  val none: Virtuality = Value(0x00)
  val virtual: Virtuality = Value(0x01)
  val pure_virtual: Virtuality = Value(0x02)
}
