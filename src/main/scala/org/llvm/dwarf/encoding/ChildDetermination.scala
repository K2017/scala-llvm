package org.llvm.dwarf.encoding

object ChildDetermination extends Enumeration {
  type ChildDetermination = Value

  val no: ChildDetermination = Value(0x00)
  val yes: ChildDetermination = Value(0x01)
}
