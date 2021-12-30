package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class DIFile(val llvmDIFile: LLVMMetadataRef) extends DIScope(llvmDIFile) {

}
