package org.llvm.dwarf

import org.bytedeco.llvm.LLVM._

class DILocation(val llvmDILocation: LLVMMetadataRef) extends MetadataNode(llvmDILocation) {

}

object DILocation {
}
