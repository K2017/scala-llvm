package org.llvm.dwarf.encoding

object Endianity extends Enumeration {
  type Endianity = Value

  val default: Endianity = Value(0x00)
  val big: Endianity = Value(0x01)
  val little: Endianity = Value(0x02)
  val lo_user: Endianity = Value(0x40)
  val hi_user: Endianity = Value(0xff)
}
