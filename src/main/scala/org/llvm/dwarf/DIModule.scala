package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class DIModule(val llvmDIModule: LLVMMetadataRef) extends DIScope(llvmDIModule) {

}
