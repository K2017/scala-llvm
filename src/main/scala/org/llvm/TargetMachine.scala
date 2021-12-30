package org.llvm

import org.bytedeco.javacpp.BytePointer
import org.bytedeco.llvm.LLVM.LLVMTargetMachineRef
import org.bytedeco.llvm.global.LLVM.{LLVMCreateTargetDataLayout, LLVMCreateTargetMachine, LLVMDisposeMessage, LLVMGetTargetMachineTarget, LLVMTargetMachineEmitToFile}

import scala.language.implicitConversions

class TargetMachine(val llvmTargetMachineRef: LLVMTargetMachineRef, val triple: String) extends LLVMObjectWrapper {
  override val llvmObject: LLVMTargetMachineRef = llvmTargetMachineRef
  val target: Target = new Target(LLVMGetTargetMachineTarget(this), triple)

  def createDataLayout() = new TargetData(LLVMCreateTargetDataLayout(this))

  def emitToFile(module: Module, file: String): Option[String] = {
    val errorMsg = new BytePointer()
    LLVMTargetMachineEmitToFile(this, module.llvmModule, new BytePointer(file), 1, errorMsg) match {
      case 0 => None
      case _ =>
        val msg = errorMsg.getString
        LLVMDisposeMessage(errorMsg)
        Some(msg)
    }
  }
}

object TargetMachine {
  implicit def toLLVM(tm: TargetMachine): LLVMTargetMachineRef = tm.llvmTargetMachineRef

  def createDefault(): Either[TargetMachine, String] = {
    Target.createDefault() match {
      case Left(target) => Left(fromTarget(target))
      case Right(msg) => Right(msg)
    }
  }

  def fromTarget(target: Target, cpu: String = "generic", features: String = ""): TargetMachine = {
    new TargetMachine(LLVMCreateTargetMachine(target, target.triple, cpu, features, 2, 0, 0), target.triple)
  }
}