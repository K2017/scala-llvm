package org.llvm.dwarf

import scala.language.implicitConversions

object Flag {
  sealed class Flag(val id: Int) {
    def |(other: Flag): Flag = new Flag(id | other.id)
  }

  case object Zero extends Flag(0)

  case object Private extends Flag(1)

  case object Protected extends Flag(2)

  case object Public extends Flag(3)

  case object FwdDecl extends Flag(1 << 2)

  case object AppleBlock extends Flag(1 << 3)

  case object ReservedBit4 extends Flag(1 << 4)

  case object Virtual extends Flag(1 << 5)

  case object Artificial extends Flag(1 << 6)

  case object Explicit extends Flag(1 << 7)

  case object Prototyped extends Flag(1 << 8)

  case object ObjcClassComplete extends Flag(1 << 9)

  case object ObjectPointer extends Flag(1 << 10)

  case object Vector extends Flag(1 << 11)

  case object StaticMember extends Flag(1 << 12)

  case object LValueReference extends Flag(1 << 13)

  case object RValueReference extends Flag(1 << 14)

  case object Reserved extends Flag(1 << 15)

  case object SingleInheritance extends Flag(1 << 16)

  case object MultipleInheritance extends Flag(2 << 16)

  case object VirtualInheritance extends Flag(3 << 16)

  case object IntroducedVirtual extends Flag(1 << 18)

  case object BitField extends Flag(1 << 19)

  case object NoReturn extends Flag(1 << 20)

  case object TypePassByValue extends Flag(1 << 22)

  case object TypePassByReference extends Flag(1 << 23)

  case object EnumClass extends Flag(1 << 24)

  @deprecated
  case object FixedEnum extends Flag(EnumClass.id)

  case object Thunk extends Flag(1 << 25)

  case object NonTrivial extends Flag(1 << 26)

  case object BigEndian extends Flag(1 << 27)

  case object LittleEndian extends Flag(1 << 28)

  case object IndirectVirtualBase extends Flag((1 << 2) | (1 << 5))

  case object Accessibility extends Flag(Private.id | Protected.id | Public.id)

  case object PtrToMemberRep extends Flag(SingleInheritance.id | MultipleInheritance.id | VirtualInheritance.id)
}