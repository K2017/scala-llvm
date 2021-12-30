package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

private[llvm] abstract class DILexicalBlockBase(val llvmDILexicalBlockBase: LLVMMetadataRef) extends DILocalScope(llvmDILexicalBlockBase) {

}

class DILexicalBlock(val llvmDILexicalBlock: LLVMMetadataRef) extends DILexicalBlockBase(llvmDILexicalBlock) {}
class DILexicalBlockFile(val llvmDILexicalBlockFile: LLVMMetadataRef) extends DILexicalBlockBase(llvmDILexicalBlockFile) {}

