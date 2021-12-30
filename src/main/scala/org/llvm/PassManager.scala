package org.llvm

import org.bytedeco.llvm.LLVM.{LLVMPassManagerRef, LLVMTargetMachineRef}
import org.bytedeco.llvm.global.LLVM.{LLVMAddAggressiveDCEPass, LLVMAddAlignmentFromAssumptionsPass, LLVMAddCFGSimplificationPass, LLVMAddConstantMergePass, LLVMAddDeadArgEliminationPass, LLVMAddDeadStoreEliminationPass, LLVMAddFunctionAttrsPass, LLVMAddGVNPass, LLVMAddGlobalDCEPass, LLVMAddGlobalOptimizerPass, LLVMAddInstructionCombiningPass, LLVMAddInstructionSimplifyPass, LLVMAddMergedLoadStoreMotionPass, LLVMAddNewGVNPass, LLVMAddPromoteMemoryToRegisterPass, LLVMAddReassociatePass, LLVMAddSCCPPass, LLVMAddStripDeadPrototypesPass, LLVMAddTypeBasedAliasAnalysisPass, LLVMCreatePassBuilderOptions, LLVMCreatePassManager, LLVMDisposeMessage, LLVMDisposePassManager, LLVMGetErrorMessage, LLVMRunPassManager, LLVMRunPasses}

import scala.language.implicitConversions

class PassManager(val llvmPassManager: LLVMPassManagerRef) extends LLVMObjectWrapper with Disposable {
  override val llvmObject: LLVMPassManagerRef = llvmPassManager

  def addMem2RegPass(): Unit = LLVMAddPromoteMemoryToRegisterPass(this)
  def addInstructionCombiningPass(): Unit = LLVMAddInstructionCombiningPass(this)
  def addInstructionSimplifyPass(): Unit = LLVMAddInstructionSimplifyPass(this)
  def addStripDeadPrototypesPass(): Unit = LLVMAddStripDeadPrototypesPass(this)
  def addAggressiveDCEPass(): Unit = LLVMAddAggressiveDCEPass(this)
  def addMergedLoadStoreMotionPass(): Unit = LLVMAddMergedLoadStoreMotionPass(this)
  def addDeadArgEliminationPass(): Unit = LLVMAddDeadArgEliminationPass(this)
  def addDeadStoreEliminationPass(): Unit = LLVMAddDeadStoreEliminationPass(this)
  def addGlobalDCEPass(): Unit = LLVMAddGlobalDCEPass(this)
  def addGlobalOptimizerPass(): Unit = LLVMAddGlobalOptimizerPass(this)
  def addConstantMergePass(): Unit = LLVMAddConstantMergePass(this)
  def addReassociatePass(): Unit = LLVMAddReassociatePass(this)
  def addSCCPPass(): Unit = LLVMAddSCCPPass(this)
  def addNewGVNPass(): Unit = LLVMAddNewGVNPass(this)
  @deprecated("Use addNewGVNPass for up-to-date GVN pass")
  def addGVNPass(): Unit = LLVMAddGVNPass(this)
  def addCFGSimplificationPass(): Unit = LLVMAddCFGSimplificationPass(this)
  def addFunctionAttrsPass(): Unit = LLVMAddFunctionAttrsPass(this)
  def addAlignmentFromAssumptionsPass(): Unit = LLVMAddAlignmentFromAssumptionsPass(this)
  def addTypeBasedAliasAnalysisPass(): Unit = LLVMAddTypeBasedAliasAnalysisPass(this)

  def runPreparedPasses(module: Module): Int = LLVMRunPassManager(this, module)

  def runNewPasses(module: Module, passes: String, targetMachine: LLVMTargetMachineRef = null): Option[String] = {
    val opts = LLVMCreatePassBuilderOptions()
    LLVMRunPasses(module, passes, targetMachine, opts) match {
      case null => None
      case err =>
        val errStr = LLVMGetErrorMessage(err)
        LLVMDisposeMessage(errStr)
        Some(errStr.getString)
    }
  }

  override protected def doDispose(): Unit = LLVMDisposePassManager(this)
}

object PassManager {
  implicit def passManagerToLLVM(manager: PassManager): LLVMPassManagerRef = manager.llvmPassManager
  def create(): PassManager = new PassManager(LLVMCreatePassManager())
}
