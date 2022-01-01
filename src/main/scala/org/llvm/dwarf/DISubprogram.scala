package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef
import org.bytedeco.llvm.global.LLVM._

class DISubprogram(val llvmDISubprogram: LLVMMetadataRef) extends DILocalScope(llvmDISubprogram) {

  def getLine: Int = LLVMDISubprogramGetLine(this)
//  def finalizeSubprogram(): Unit = LLVMDIBuilderFinalizeSubprogram(this)

}
