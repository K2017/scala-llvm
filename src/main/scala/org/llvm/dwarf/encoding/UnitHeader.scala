package org.llvm.dwarf.encoding

object UnitHeader extends Enumeration {
  type UnitHeader = Value

  val compile: UnitHeader = Value(0x01)
  val Type: UnitHeader = Value(0x02)
  val partial: UnitHeader = Value(0x03)
  val skeleton: UnitHeader = Value(0x04)
  val split_compile: UnitHeader = Value(0x05)
  val split_type: UnitHeader = Value(0x06)
  val lo_user: UnitHeader = Value(0x80)
  val hi_user: UnitHeader = Value(0xff)
}
