package org.llvm

import org.bytedeco.javacpp.{BytePointer, PointerPointer}
import org.bytedeco.llvm.LLVM.{LLVMBuilderRef, LLVMValueRef}
import org.bytedeco.llvm.global.LLVM.{LLVMBuildAdd, LLVMBuildAlloca, LLVMBuildAnd, LLVMBuildBitCast, LLVMBuildBr, LLVMBuildCall, LLVMBuildCondBr, LLVMBuildFAdd, LLVMBuildFDiv, LLVMBuildFMul, LLVMBuildFSub, LLVMBuildFree, LLVMBuildGEP, LLVMBuildGEP2, LLVMBuildGlobalStringPtr, LLVMBuildICmp, LLVMBuildIntToPtr, LLVMBuildIsNotNull, LLVMBuildIsNull, LLVMBuildLoad, LLVMBuildMalloc, LLVMBuildMul, LLVMBuildNot, LLVMBuildOr, LLVMBuildPhi, LLVMBuildPtrToInt, LLVMBuildRet, LLVMBuildRetVoid, LLVMBuildSelect, LLVMBuildStore, LLVMBuildStructGEP, LLVMBuildSub, LLVMBuildUDiv, LLVMConstNull, LLVMCreateBuilderInContext, LLVMDisposeBuilder, LLVMGetBasicBlockParent, LLVMGetBasicBlockTerminator, LLVMGetInsertBlock, LLVMPositionBuilderAtEnd, LLVMSetCurrentDebugLocation2}
import org.llvm.IntPredicate.IntPredicate
import org.llvm.dwarf.DILocation

import scala.language.implicitConversions

class Builder(private implicit val module: Module) extends Disposable {
  val m: Module = module // non-implicit reference to module
  val llvmBuilder: LLVMBuilderRef = LLVMCreateBuilderInContext(module.context)
  private val NO_NAME: String = ""

  def add(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildAdd(this, v1, v2, NO_NAME))
  def fadd(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildFAdd(this, v1, v2, NO_NAME))

  def sub(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildSub(this, v1, v2, NO_NAME))
  def fsub(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildFSub(this, v1, v2, NO_NAME))

  def mul(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildMul(this, v1, v2, NO_NAME))
  def fmul(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildFMul(this, v1, v2, NO_NAME))

  def div(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildUDiv(this, v1, v2, NO_NAME))
  def fdiv(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildFDiv(this, v1, v2, NO_NAME))

  def and(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildAnd(this, v1, v2, NO_NAME))
  def or(v1: Value, v2: Value): SSAValue = new SSAValue(LLVMBuildOr(this, v1, v2, NO_NAME))
  def not(v1: Value): SSAValue = new SSAValue(LLVMBuildNot(this, v1, NO_NAME))

  def icmp(v1: Value, v2: Value, pred: IntPredicate): SSAValue = new SSAValue(LLVMBuildICmp(this, pred.id, v1, v2, NO_NAME))

  def insertAtEndOfBlock(block: BasicBlock): Unit = LLVMPositionBuilderAtEnd(this, block)
  def currentBlock: BasicBlock = BasicBlock(LLVMGetInsertBlock(this))
  def currentFunction: Function = new Function(LLVMGetBasicBlockParent(LLVMGetInsertBlock(this)))

  def ret(v: Value): Instruction = new Instruction(LLVMBuildRet(this, v))
  def ret(): Instruction = new Instruction(LLVMBuildRetVoid(this))

  def br(destBlock: BasicBlock): Instruction = new Instruction(LLVMBuildBr(this, destBlock))
  def condBr(condition: Value, trueBlock: BasicBlock, falseBlock: BasicBlock): Instruction = {
    new Instruction(LLVMBuildCondBr(this, condition, trueBlock, falseBlock))
  }
  def select(cond: Value, thn: Value, otherwise: Value): Instruction = new Instruction(LLVMBuildSelect(this, cond, thn, otherwise, NO_NAME))

  def PHI(t: Type): PHINode = new PHINode(LLVMBuildPhi(this, t, NO_NAME))
  def call(fn: Value, args: Value*): Instruction = new Instruction({
    val argsPointer = new PointerPointer[LLVMValueRef](args.map(_.llvmValue): _*)
    LLVMBuildCall(this, fn, argsPointer, args.length, NO_NAME)
  })

  def alloc(t: Type): SSAValue = new SSAValue(LLVMBuildAlloca(this, t, NO_NAME))
  def malloc(t: Type): SSAValue = new SSAValue(LLVMBuildMalloc(this, t, NO_NAME))
  def free(ptr: Value): Instruction = new Instruction(LLVMBuildFree(this, ptr))
  def isNull(ptr: Value): SSAValue = new SSAValue(LLVMBuildIsNull(this, ptr, NO_NAME))
  def isNotNull(ptr: Value): SSAValue = new SSAValue(LLVMBuildIsNotNull(this, ptr, NO_NAME))

  def strLit(str: String): Instruction = new Instruction(LLVMBuildGlobalStringPtr(this, str, NO_NAME))
  def nullLit(tp: Type): SSAValue = new SSAValue(LLVMConstNull(tp))

  def store(ptr: Value, v: Value): Instruction = new Instruction(LLVMBuildStore(this, v, ptr))
  def load(ptr: Value): SSAValue = new SSAValue(LLVMBuildLoad(this, ptr, NO_NAME))

  def bitcast(v: Value, to: Type): SSAValue = new SSAValue(LLVMBuildBitCast(this, v, to, NO_NAME))
  def ptrToInt(v: Value, to: Type): SSAValue = new SSAValue(LLVMBuildPtrToInt(this, v, to, NO_NAME))
  def intToPtr(v: Value, to: Type): SSAValue = new SSAValue(LLVMBuildIntToPtr(this, v, to, NO_NAME))
  def sizeof(t: Type): SSAValue = {
    val tNull = nullLit(t.*)
    val size = new SSAValue(
      LLVMBuildGEP2(this, t, tNull, Value.from(1).llvmValue, 1, new BytePointer(NO_NAME))
    )
    ptrToInt(size, module.context.Types.i32)
  }
  def deref(ptr: Value, tp: Type, idx: Value): SSAValue = new SSAValue(LLVMBuildGEP2(this, tp, ptr, idx, 1, new BytePointer(NO_NAME)))

  def getBasicBlockTerminator(block: BasicBlock): Option[Instruction] = LLVMGetBasicBlockTerminator(block) match {
    case null => None
    case value => Some(new Instruction(value))
  }

  def getElement(ptr: Value, idx: Int): SSAValue = new SSAValue(
    LLVMBuildStructGEP(this, ptr, idx, NO_NAME)
  )

  def setCurrentDebugLocation(loc: DILocation): Unit = LLVMSetCurrentDebugLocation2(this, loc)
  def clearCurrentDebugLocation(): Unit = LLVMSetCurrentDebugLocation2(this, null)

  protected def doDispose(): Unit = LLVMDisposeBuilder(this)
}

object Builder {
  implicit def builderToLLVM(builder: Builder): LLVMBuilderRef = builder.llvmBuilder
}
