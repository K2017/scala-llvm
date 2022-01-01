package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef
import org.bytedeco.llvm.global.LLVM.LLVMDIScopeGetFile

class DIScope(val scope: LLVMMetadataRef) extends DINode(scope) {

  def getFile: DIFile = new DIFile(LLVMDIScopeGetFile(this))
}

class DILocalScope(val llvmDILocalScope: LLVMMetadataRef) extends DIScope(llvmDILocalScope)
