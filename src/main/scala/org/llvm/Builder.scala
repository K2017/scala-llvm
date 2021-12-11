package org.llvm

import scala.collection.mutable
import scala.language.implicitConversions

class Builder(implicit val module: Module) extends Disposable {
  val llvmBuilder: api.Builder = api.LLVMCreateBuilderInContext(module.context)
  val insertPointStack = new mutable.Stack[api.tools.InsertPoint]
  private val NO_NAME: String = ""

  def add(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildAdd(this, v1, v2, NO_NAME))
  def fadd(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildFAdd(this, v1, v2, NO_NAME))

  def sub(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildSub(this, v1, v2, NO_NAME))
  def fsub(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildFSub(this, v1, v2, NO_NAME))

  def mul(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildMul(this, v1, v2, NO_NAME))
  def fmul(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildFMul(this, v1, v2, NO_NAME))

  def div(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildUDiv(this, v1, v2, NO_NAME))
  def fdiv(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildFDiv(this, v1, v2, NO_NAME))

  def and(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildAnd(this, v1, v2, NO_NAME))
  def or(v1: Value, v2: Value): SSAValue = new SSAValue(api.LLVMBuildOr(this, v1, v2, NO_NAME))
  def not(v1: Value): SSAValue = new SSAValue(api.LLVMBuildNot(this, v1, NO_NAME))

  def pushIP(): insertPointStack.type = insertPointStack.push(api.tools.LLVMSaveInsertPoint(this))

  def popIP(): Unit = {
    val ip: api.tools.InsertPoint = insertPointStack.pop()
    api.tools.LLVMRestoreInsertPoint(this, ip)
    api.tools.LLVMDisposeInsertPoint(ip)
  }

  def insertAtEndOfBlock(block: BasicBlock): Unit = api.LLVMPositionBuilderAtEnd(this, block)

  def currentBlock: BasicBlock = BasicBlock(api.LLVMGetInsertBlock(this))

  def currentFunction: Function = new Function(api.LLVMGetBasicBlockParent(api.LLVMGetInsertBlock(this)))

  def ret(v: Value): Instruction = new Instruction(api.LLVMBuildRet(this, v))

  def br(destBlock: BasicBlock): Instruction = new Instruction(api.LLVMBuildBr(this, destBlock))

  def condBr(condition: Value, trueBlock: BasicBlock, falseBlock: BasicBlock): Instruction =
    new Instruction(api.LLVMBuildCondBr(this, condition, trueBlock, falseBlock))

  def PHI(t: Type): PHINode = new PHINode(api.LLVMBuildPhi(this, t, NO_NAME))

  def getBasicBlockTerminator(block: BasicBlock): Option[Instruction] = api.LLVMGetBasicBlockTerminator(block) match {
    case null => None
    case value => Some(new Instruction(value))
  }

  protected def doDispose(): Unit = api.LLVMDisposeBuilder(this)
}

object Builder {
  implicit def builderToLLVM(builder: Builder): api.Builder = builder.llvmBuilder
}
