package org.llvm.dwarf

import scala.language.implicitConversions


/**
 * See [[Flag]]
 */
@deprecated
object Flag2 {
  sealed class Flag2(val id: Int) {
    def |(other: Flag2): Flag2 = new Flag2(id | other.id)
  }

  case object Zero extends Flag2(0)

  case object Private extends Flag2(1)

  case object Protected extends Flag2(2)

  case object Public extends Flag2(3)

  case object FwdDecl extends Flag2(1 << 2)

  case object AppleBlock extends Flag2(1 << 3)

  case object ReservedBit4 extends Flag2(1 << 4)

  case object Virtual extends Flag2(1 << 5)

  case object Artificial extends Flag2(1 << 6)

  case object Explicit extends Flag2(1 << 7)

  case object Prototyped extends Flag2(1 << 8)

  case object ObjcClassComplete extends Flag2(1 << 9)

  case object ObjectPointer extends Flag2(1 << 10)

  case object Vector extends Flag2(1 << 11)

  case object StaticMember extends Flag2(1 << 12)

  case object LValueReference extends Flag2(1 << 13)

  case object RValueReference extends Flag2(1 << 14)

  case object Reserved extends Flag2(1 << 15)

  case object SingleInheritance extends Flag2(1 << 16)

  case object MultipleInheritance extends Flag2(2 << 16)

  case object VirtualInheritance extends Flag2(3 << 16)

  case object IntroducedVirtual extends Flag2(1 << 18)

  case object BitField extends Flag2(1 << 19)

  case object NoReturn extends Flag2(1 << 20)

  case object TypePassByValue extends Flag2(1 << 22)

  case object TypePassByReference extends Flag2(1 << 23)

  case object EnumClass extends Flag2(1 << 24)

  @deprecated
  case object FixedEnum extends Flag2(EnumClass.id)

  case object Thunk extends Flag2(1 << 25)

  case object NonTrivial extends Flag2(1 << 26)

  case object BigEndian extends Flag2(1 << 27)

  case object LittleEndian extends Flag2(1 << 28)

  case object IndirectVirtualBase extends Flag2((1 << 2) | (1 << 5))

  case object Accessibility extends Flag2(Private.id | Protected.id | Public.id)

  case object PtrToMemberRep extends Flag2(SingleInheritance.id | MultipleInheritance.id | VirtualInheritance.id)
}