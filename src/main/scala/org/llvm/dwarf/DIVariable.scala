package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef
import org.bytedeco.llvm.global.LLVM._

class DIVariable(val llvmDIVariableRef: LLVMMetadataRef) extends DINode(llvmDIVariableRef) {

  def getFile = new DIFile(LLVMDIVariableGetFile(this))
  def getScope = new DIScope(LLVMDIVariableGetScope(this))
  def getLine: Int = LLVMDIVariableGetLine(this)

}

class DILocalVariable(val llvmDILocalVariableRef: LLVMMetadataRef) extends DIVariable(llvmDILocalVariableRef)
class DIGlobalVariable(val llvmDIGlobalVariableRef: LLVMMetadataRef) extends DIVariable(llvmDIGlobalVariableRef)
