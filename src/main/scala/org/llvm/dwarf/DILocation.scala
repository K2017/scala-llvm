package org.llvm.dwarf

import org.bytedeco.llvm.LLVM._
import org.bytedeco.llvm.global.LLVM.{LLVMDILocationGetColumn, LLVMDILocationGetLine, LLVMDILocationGetScope}

class DILocation(val llvmDILocation: LLVMMetadataRef) extends MetadataNode(llvmDILocation) {

  def getLine: Int = LLVMDILocationGetLine(this)
  def getColumn: Int = LLVMDILocationGetColumn(this)

  def getScope: DIScope = new DIScope(LLVMDILocationGetScope(this))

}

object DILocation {
}
