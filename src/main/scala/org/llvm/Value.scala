package org.llvm

import org.llvm.Linkage.Linkage
import org.llvm.Visibility.Visibility

import scala.language.implicitConversions

trait Value extends LLVMObjectWrapper {
  val llvmValue: api.Value
  val llvmObject: api.GenericObject = llvmValue
  implicit val module: Module
  implicit val context: Context = module.context

  override def toString: String = {
    val ptr = api.LLVMPrintValueToString(this)
    val str = ptr.getString(0)
    api.LLVMDisposeMessage(ptr)
    str
  }

  def getType: Type = Type.resolveLLVMType(api.LLVMTypeOf(this))
  def name: String = api.LLVMGetValueName(this)

  def <(other: Value)(implicit builder: Builder): SSAValue = builder.icmp(this, other, IntPredicate.SLT)
  def <=(other: Value)(implicit builder: Builder): SSAValue = builder.icmp(this, other, IntPredicate.SLE)
  def >(other: Value)(implicit builder: Builder): SSAValue = builder.icmp(this, other, IntPredicate.SGT)
  def >=(other: Value)(implicit builder: Builder): SSAValue = builder.icmp(this, other, IntPredicate.SGE)
  def ==(other: Value)(implicit builder: Builder): SSAValue = builder.icmp(this, other, IntPredicate.EQ)
  def !=(other: Value)(implicit builder: Builder): SSAValue = builder.icmp(this, other, IntPredicate.NE)

  /** Same-type sum. Will throw error if values are not of the same type */
  def +(other: Value)(implicit builder: Builder): SSAValue = builder.add(this, other)
  def -(other: Value)(implicit builder: Builder): SSAValue = builder.sub(this, other)
  def *(other: Value)(implicit builder: Builder): SSAValue = builder.mul(this, other)
  def /(other: Value)(implicit builder: Builder): SSAValue = builder.div(this, other)
  def &&(other: Value)(implicit builder: Builder): SSAValue = builder.and(this, other)
  def ||(other: Value)(implicit builder: Builder): SSAValue = builder.or(this, other)
  def unary_!(implicit builder: Builder): SSAValue = builder.not(this)

  def setName(name: String): this.type = { api.LLVMSetValueName(this, name); this }
  def as(name: String): this.type = setName(name)

  def setLinkage(linkage: Linkage): Unit = api.LLVMSetLinkage(this, linkage.id)
  def setVisibility(visibility: Visibility): Unit = api.LLVMSetVisibility(this, visibility.id)
}

object Value {
  implicit def valueToLLVM(value: Value): api.Value = value.llvmValue
  implicit def toBoolean(value: Value): Boolean = {
    import value.module.context.Types._
    implicit val module: Module = value.module
    value.getType == i1 && value == Value.from(true)
  }
  /** Helper function that casts constants automatically */
  def from[T](value: T)(implicit module: Module): Value = value match {
    case v: Value => v
    case _ => fromConstant(value)
  }

  def fromConstant[T](value: T)(implicit module: Module): Constant = {
    import module.context.Types._
    value match {
      case v: Int => new Constant(api.LLVMConstInt(i32, v, 0))
      case v: Float => new Constant(api.LLVMConstReal(float, v))
      case v: Boolean => new Constant(api.LLVMConstInt(i1, if (v) 1 else 0, 0))

      case _ => throw new LLVMException(s"Cannot cast value '$value' to LLVM native value")
    }
  }
}

abstract class BaseValue(val llvmValue: api.Value, val module: Module) extends Value {
}

trait Variable extends Value {
  def valueType: Type = getType.asInstanceOf[PointerType].pointedType
  def fetchValue(implicit builder: Builder): SSAValue = new SSAValue(api.LLVMBuildLoad(builder, this, ""))(builder.m)
  def storeValue(v: Value)(implicit builder: Builder): Instruction = builder.store(this, v)
}

class Constant(llvmValue: api.Value)(implicit module: Module) extends BaseValue(llvmValue, module)
class Instruction(llvmValue: api.Value)(implicit module: Module) extends BaseValue(llvmValue, module)
class SSAValue(llvmValue: api.Value)(implicit module: Module) extends Instruction(llvmValue)
class GlobalVariable(llvmValue: api.Value)(implicit module: Module) extends BaseValue(llvmValue, module) with Variable
class PHINode(llvmValue: api.Value)(implicit module: Module) extends SSAValue(llvmValue) {
  def <~(p: (BasicBlock, Value)): this.type = {
    ???
    this
  }

  def <~(value: Value)(implicit builder: Builder): this.type = {
    val block = builder.currentBlock
    this <~ (block, value)
  }
}
