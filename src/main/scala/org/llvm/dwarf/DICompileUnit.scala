package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class DICompileUnit(val llvmDICompileUnit: LLVMMetadataRef) extends DIScope(llvmDICompileUnit) {

}
