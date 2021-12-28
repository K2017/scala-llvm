package org.llvm.dwarf.encoding

object Accessibility extends Enumeration {
  type Accessibility = Value

  val Public: Accessibility = Value(0x01)
  val Protected: Accessibility = Value(0x02)
  val Private: Accessibility = Value(0x03)
}
