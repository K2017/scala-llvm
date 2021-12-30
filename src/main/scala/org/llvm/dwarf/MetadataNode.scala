package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class MetadataNode(val metadata: LLVMMetadataRef) extends Metadata(metadata) {

}
