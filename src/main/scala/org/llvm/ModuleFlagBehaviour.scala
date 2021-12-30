package org.llvm

import org.bytedeco.llvm.global.LLVM.{LLVMModuleFlagBehaviorAppend, LLVMModuleFlagBehaviorAppendUnique, LLVMModuleFlagBehaviorError, LLVMModuleFlagBehaviorOverride, LLVMModuleFlagBehaviorRequire, LLVMModuleFlagBehaviorWarning}

object ModuleFlagBehaviour extends Enumeration {
  type ModuleFlagBehaviour = Value

  val Error: ModuleFlagBehaviour = Value(LLVMModuleFlagBehaviorError)
  val Warning: ModuleFlagBehaviour = Value(LLVMModuleFlagBehaviorWarning)
  val Require: ModuleFlagBehaviour = Value(LLVMModuleFlagBehaviorRequire)
  val Override: ModuleFlagBehaviour = Value(LLVMModuleFlagBehaviorOverride)
  val Append: ModuleFlagBehaviour = Value(LLVMModuleFlagBehaviorAppend)
  val AppendUnique: ModuleFlagBehaviour = Value(LLVMModuleFlagBehaviorAppendUnique)
}
