package org.llvm.dwarf

import org.bytedeco.javacpp.IntPointer
import org.bytedeco.llvm.LLVM.LLVMMetadataRef
import org.bytedeco.llvm.global.LLVM._

class DIFile(val llvmDIFile: LLVMMetadataRef) extends DIScope(llvmDIFile) {

  def getDirectory: String = {
    val len = new IntPointer(1L)
    LLVMDIFileGetDirectory(this, len).getString
  }
  def getFilename: String = {
    val len = new IntPointer(1L)
    LLVMDIFileGetFilename(this, len).getString
  }
  def getSource: String = {
    val len = new IntPointer(1L)
    LLVMDIFileGetSource(this, len).getString
  }

}
