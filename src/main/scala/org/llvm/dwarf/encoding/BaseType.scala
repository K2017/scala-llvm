package org.llvm.dwarf.encoding

object BaseType extends Enumeration {
  type BaseType = Value

  val address: BaseType = Value(0x01)
  val boolean: BaseType = Value(0x02)
  val complex_float: BaseType = Value(0x03)
  val float: BaseType = Value(0x04)
  val signed: BaseType = Value(0x05)
  val signed_char: BaseType = Value(0x06)
  val unsigned: BaseType = Value(0x07)
  val unsigned_char: BaseType = Value(0x08)
  val imaginary_float: BaseType = Value(0x09)
  val packed_decimal: BaseType = Value(0x0a)
  val numeric_string: BaseType = Value(0x0b)
  val edited: BaseType = Value(0x0c)
  val signed_fixed: BaseType = Value(0x0d)
  val unsigned_fixed: BaseType = Value(0x0e)
  val decimal_float: BaseType = Value(0x0f)
  val UTF: BaseType = Value(0x10)
  val UCS: BaseType = Value(0x11)
  val ASCII: BaseType = Value(0x12)
  val lo_user: BaseType = Value(0x80)
  val hi_user: BaseType = Value(0xff)
}
