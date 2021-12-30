package org.llvm.dwarf.encoding

object Endianity extends Enumeration {
  type Endianity = Value

  val default: Endianity = Value(0x00)
  val big: Endianity = Value(0x01)
  val little: Endianity = Value(0x02)
}
