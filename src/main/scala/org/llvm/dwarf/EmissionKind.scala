package org.llvm.dwarf

object EmissionKind extends Enumeration {
  type EmissionKind = Value

  val NoDebug: EmissionKind = Value(0)
  val FullDebug: EmissionKind = Value(1)
  val LineTablesOnly: EmissionKind = Value(2)
  val DebugDirectivesOnly: EmissionKind = Value(3)
  val LastEmissionKind: EmissionKind = DebugDirectivesOnly
}
