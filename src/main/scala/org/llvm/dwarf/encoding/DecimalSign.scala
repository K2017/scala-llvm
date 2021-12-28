package org.llvm.dwarf.encoding

object DecimalSign extends Enumeration {
  type DecimalSign = Value

  val unsigned: DecimalSign = Value(0x01)
  val leading_overpunch: DecimalSign = Value(0x02)
  val trailing_overpunch: DecimalSign = Value(0x03)
  val leading_separate: DecimalSign = Value(0x04)
  val trailing_separate: DecimalSign = Value(0x05)
}
