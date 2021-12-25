package org.llvm

import scala.language.implicitConversions

case class BasicBlock(llvmBasicBlock: api.BasicBlock) {
  def build(bodyBuilder: => Unit)(implicit builder: Builder): this.type = {
    val previous = builder.currentBlock
    builder.insertAtEndOfBlock(this)
    bodyBuilder
    builder.insertAtEndOfBlock(previous)
    this
  }
  def :=(bodyBuilder: => Unit)(implicit builder: Builder) : this.type = build(bodyBuilder)(builder)
  def apply(bodyBuilder: => Unit)(implicit builder: Builder): BasicBlock.this.type = build(bodyBuilder)(builder)
}

object BasicBlock {
  implicit def basicBlockToLLVM(bb: BasicBlock): api.BasicBlock = bb.llvmBasicBlock
}

