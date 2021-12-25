package org.llvm

object IntPredicate extends Enumeration {
  type IntPredicate = Value

  /** < equal */
  val EQ: IntPredicate = Value(32)

  /** < not equal */
  val NE: IntPredicate = Value(33)

  /** < unsigned greater than */
  val UGT: IntPredicate = Value(34)

  /** < unsigned greater or equal */
  val UGE: IntPredicate = Value(35)

  /** < unsigned less than */
  val ULT: IntPredicate = Value(36)

  /** < unsigned less or equal */
  val ULE: IntPredicate = Value(37)

  /** < signed greater than */
  val SGT: IntPredicate = Value(38)

  /** < signed greater or equal */
  val SGE: IntPredicate = Value(39)

  /** < signed less than */
  val SLT: IntPredicate = Value(40)

  /** < signed less or equal */
  val SLE: IntPredicate = Value(41)
}
