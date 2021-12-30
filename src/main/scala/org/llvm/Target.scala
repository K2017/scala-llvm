package org.llvm

import org.bytedeco.javacpp.{BytePointer, PointerPointer}
import org.bytedeco.llvm.LLVM.LLVMTargetRef
import org.bytedeco.llvm.global.LLVM.{LLVMDisposeMessage, LLVMGetDefaultTargetTriple, LLVMGetTargetFromTriple, LLVMGetTargetName}

import scala.language.implicitConversions

class Target(val llvmTargetRef: LLVMTargetRef, val triple: String) extends LLVMObjectWrapper {
  override val llvmObject: LLVMTargetRef = llvmTargetRef

  val name: String = LLVMGetTargetName(this).getString
}

object Target {
  implicit def toLLVM(t: Target): LLVMTargetRef = t.llvmTargetRef

  def createDefault(): Either[Target, String] = {
    val triple = getDefaultTriple
    fromTargetTriple(triple)
  }

  def getDefaultTriple: String = LLVMGetDefaultTargetTriple().getString

  def fromTargetTriple(triple: String): Either[Target, String] = {
    val target = new PointerPointer[LLVMTargetRef](1)
    val errorMsg = new BytePointer()
    LLVMGetTargetFromTriple(triple, target, errorMsg) match {
      case 0 => Left(new Target(target.get(classOf[LLVMTargetRef]), triple))
      case _ =>
        val msg = errorMsg.getString
        LLVMDisposeMessage(errorMsg)
        Right(msg)
    }
  }
}