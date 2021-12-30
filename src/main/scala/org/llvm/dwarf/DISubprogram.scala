package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef
import org.bytedeco.llvm.global.LLVM

class DISubprogram(val llvmDISubprogram: LLVMMetadataRef) extends DILocalScope(llvmDISubprogram) {

//  def finalizeSubprogram(): Unit = LLVMDIBuilderFinalizeSubprogram(this)

}
