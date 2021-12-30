package org.llvm

import org.bytedeco.llvm.LLVM.LLVMTargetDataRef
import org.bytedeco.llvm.global.LLVM.{LLVMIntPtrType, LLVMOffsetOfElement, LLVMPointerSize, LLVMPreferredAlignmentOfType, LLVMSizeOfTypeInBits}

import scala.language.implicitConversions

class TargetData(val llvmTargetDataRef: LLVMTargetDataRef) extends LLVMObjectWrapper {
  override val llvmObject: LLVMTargetDataRef = llvmTargetDataRef

  def getPtrSizeInBits: Int = 8 * getPtrSizeIntBytes

  def getPtrSizeIntBytes: Int = LLVMPointerSize(this)

  def getPtrAlignInBytes: Int = LLVMPreferredAlignmentOfType(this, LLVMIntPtrType(this))

  def getPtrAlignInBits: Int = 8 * getPtrAlignInBytes

  def getTypeSizeInBits(tp: Type): Long = LLVMSizeOfTypeInBits(this, tp)

  def getPreferredAlignInBytes(tp: Type): Int = LLVMPreferredAlignmentOfType(this, tp)

  def getPreferredAlignInBits(tp: Type): Int = 8 * getPreferredAlignInBytes(tp)

  def getOffsetOfElement(struct: StructType, idx: Int): Long = LLVMOffsetOfElement(this, struct, idx)
}

object TargetData {
  implicit def toLLVM(td: TargetData): LLVMTargetDataRef = td.llvmTargetDataRef
}