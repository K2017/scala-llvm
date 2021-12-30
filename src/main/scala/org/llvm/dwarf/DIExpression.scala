package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class DIExpression(val llvmDIExpression: LLVMMetadataRef) extends MetadataNode(llvmDIExpression) {

}
