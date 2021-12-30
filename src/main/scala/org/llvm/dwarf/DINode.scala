package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class DINode(val node: LLVMMetadataRef) extends MetadataNode(node) {

}
