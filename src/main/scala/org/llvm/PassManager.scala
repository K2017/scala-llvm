package org.llvm
import org.llvm.api.GenericObject

import scala.language.implicitConversions

class PassManager(val llvmPassManager: api.PassManager) extends LLVMObjectWrapper with Disposable {
  override val llvmObject: GenericObject = llvmPassManager

  def addMem2RegPass(): Unit = api.LLVMAddPromoteMemoryToRegisterPass(this)
  def addInstructionCombiningPass(): Unit = api.LLVMAddInstructionCombiningPass(this)
//  def addInstructionSimplifyPass(): Unit = api.LLVMAddInstructionSimplifyPass(this)
  def addStripDeadPrototypesPass(): Unit = api.LLVMAddStripDeadPrototypesPass(this)
  def addAggressiveDCEPass(): Unit = api.LLVMAddAggressiveDCEPass(this)
  def addMergedLoadStoreMotionPass(): Unit = api.LLVMAddMergedLoadStoreMotionPass(this)
  def addDeadArgEliminationPass(): Unit = api.LLVMAddDeadArgEliminationPass(this)
  def addDeadStoreEliminationPass(): Unit = api.LLVMAddDeadStoreEliminationPass(this)
  def addGlobalDCEPass(): Unit = api.LLVMAddGlobalDCEPass(this)
  def addGlobalOptimizerPass(): Unit = api.LLVMAddGlobalOptimizerPass(this)
  def addConstantMergePass(): Unit = api.LLVMAddConstantMergePass(this)
  def addReassociatePass(): Unit = api.LLVMAddReassociatePass(this)
  def addSCCPPass(): Unit = api.LLVMAddSCCPPass(this)

  def runPreparedPasses(module: Module): Int = api.LLVMRunPassManager(this, module)

  override protected def doDispose(): Unit = api.LLVMDisposePassManager(this)
}

object PassManager {
  implicit def passManagerToLLVM(manager: PassManager): api.PassManager = manager.llvmPassManager
  def create(): PassManager = new PassManager(api.LLVMCreatePassManager())
}
