package org.llvm.dwarf.encoding

object Language extends Enumeration {
  type Language = Value

  val C89: Language = Value(0x0001)
  val C: Language = Value(0x0002)
  val Ada83: Language = Value(0x0003)
  val C_plus_plus: Language = Value(0x0004)
  val Cobol74: Language = Value(0x0005)
  val Cobol85: Language = Value(0x0006)
  val Fortran77: Language = Value(0x0007)
  val Fortran90: Language = Value(0x0008)
  val Pascal83: Language = Value(0x0009)
  val Modula2: Language = Value(0x000a)
  val Java: Language = Value(0x000b)
  val C99: Language = Value(0x000c)
  val Ada95: Language = Value(0x000d)
  val Fortran95: Language = Value(0x000e)
  val PLI: Language = Value(0x000f)
  val ObjC: Language = Value(0x0010)
  val ObjC_plus_plus: Language = Value(0x0011)
  val UPC: Language = Value(0x0012)
  val D: Language = Value(0x0013)
  val Python: Language = Value(0x0014)
  val OpenCL: Language = Value(0x0015)
  val Go: Language = Value(0x0016)
  val Modula3: Language = Value(0x0017)
  val Haskell: Language = Value(0x0018)
  val C_plus_plus_03: Language = Value(0x0019)
  val C_plus_plus_11: Language = Value(0x001a)
  val OCaml: Language = Value(0x001b)
  val Rust: Language = Value(0x001c)
  val C11: Language = Value(0x001d)
  val Swift: Language = Value(0x001e)
  val Julia: Language = Value(0x001f)
  val Dylan: Language = Value(0x0020)
  val C_plus_plus_14: Language = Value(0x0021)
  val Fortran03: Language = Value(0x0022)
  val Fortran08: Language = Value(0x0023)
  val RenderScript: Language = Value(0x0024)
  val BLISS: Language = Value(0x0025)
}
