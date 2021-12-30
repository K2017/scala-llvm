package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

class DIVariable(val llvmDIVariableRef: LLVMMetadataRef) extends DINode(llvmDIVariableRef) {

}

class DILocalVariable(val llvmDILocalVariableRef: LLVMMetadataRef) extends DIVariable(llvmDILocalVariableRef)
class DIGlobalVariable(val llvmDIGlobalVariableRef: LLVMMetadataRef) extends DIVariable(llvmDIGlobalVariableRef)
