package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class DIScope(val scope: LLVMMetadataRef) extends DINode(scope) {

}

class DILocalScope(val llvmDILocalScope: LLVMMetadataRef) extends DIScope(llvmDILocalScope)
