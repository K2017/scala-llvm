package org.llvm

object Linkage extends Enumeration {
  type Linkage = Value

  /** < Externally visible function */
  val External: Linkage = Value(0)

  val AvailableExternally: Linkage = Value(1)
  /** < Keep one copy of function when linking (inline) */
  val LinkOnceAny: Linkage = Value(2)

  /** < Same, but only replaced by something
   * equivalent. */
  val LinkOnceODR: Linkage = Value(3)

  /** < Obsolete */
  @deprecated
  val LinkOnceODRAutoHide: Linkage = Value(4)

  /** < Keep one copy of function when linking (weak) */
  val WeakAny: Linkage = Value(5)
  val WeakODR: Linkage = Value(6)

  /** < Special purpose, only applies to global arrays */
  val Appending: Linkage = Value(7)

  /** < Rename collisions when linking (static
   * functions) */
  val Internal: Linkage = Value(8)

  /** < Like Internal, but omit from symbol table */
  val Private: Linkage = Value(9)

  /** < Obsolete */
  @deprecated
  val DLLImport: Linkage = Value(10)
  /** < Obsolete */
  @deprecated
  val DLLExport: Linkage = Value(11)

  /** < ExternalWeak linkage description */
  val ExternalWeak: Linkage = Value(12)

  /** < Obsolete */
  @deprecated
  val Ghost: Linkage = Value(13)

  /** < Tentative definitions */
  val Common: Linkage = Value(14)

  /** < Like Private, but linker removes. */
  val LinkerPrivate: Linkage = Value(15)

  /** < Like LinkerPrivate, but is weak. */
  val LinkerPrivateWeak: Linkage = Value(16)
}
