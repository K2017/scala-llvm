package org.llvm.dwarf

object Flag extends Enumeration {
  type Flag = Value

  val Zero: Flag = Value(0)
  val Private: Flag = Value(1)
  val Protected: Flag = Value(2)
  val Public: Flag = Value(3)
  val FwdDecl: Flag = Value(1 << 2)
  val AppleBlock: Flag = Value(1 << 3)
  val ReservedBit4: Flag = Value(1 << 4)
  val Virtual: Flag = Value(1 << 5)
  val Artificial: Flag = Value(1 << 6)
  val Explicit: Flag = Value(1 << 7)
  val Prototyped: Flag = Value(1 << 8)
  val ObjcClassComplete: Flag = Value(1 << 9)
  val ObjectPointer: Flag = Value(1 << 10)
  val Vector: Flag = Value(1 << 11)
  val StaticMember: Flag = Value(1 << 12)
  val LValueReference: Flag = Value(1 << 13)
  val RValueReference: Flag = Value(1 << 14)
  val Reserved: Flag = Value(1 << 15)
  val SingleInheritance: Flag = Value(1 << 16)
  val MultipleInheritance: Flag = Value(2 << 16)
  val VirtualInheritance: Flag = Value(3 << 16)
  val IntroducedVirtual: Flag = Value(1 << 18)
  val BitField: Flag = Value(1 << 19)
  val NoReturn: Flag = Value(1 << 20)
  val TypePassByValue: Flag = Value(1 << 22)
  val TypePassByReference: Flag = Value(1 << 23)
  val EnumClass: Flag = Value(1 << 24)
  @deprecated
  val FixedEnum: Flag = EnumClass // Deprecated.
  val Thunk: Flag = Value(1 << 25)
  val NonTrivial: Flag = Value(1 << 26)
  val BigEndian: Flag = Value(1 << 27)
  val LittleEndian: Flag = Value(1 << 28)
  val IndirectVirtualBase: Flag = Value((1 << 2) | (1 << 5))
//  val Accessibility: Flag = Value(Private.id | Protected.id | Public.id)
//  val PtrToMemberRep: Flag = Value(SingleInheritance.id | MultipleInheritance.id | VirtualInheritance.id)
}
