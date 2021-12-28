package org.llvm.dwarf

import org.llvm.api

private[llvm] abstract class DILexicalBlockBase(val llvmDILexicalBlockBase: api.Metadata) extends DILocalScope(llvmDILexicalBlockBase) {

}

class DILexicalBlock(val llvmDILexicalBlock: api.Metadata) extends DILexicalBlockBase(llvmDILexicalBlock) {}
class DILexicalBlockFile(val llvmDILexicalBlockFile: api.Metadata) extends DILexicalBlockBase(llvmDILexicalBlockFile) {}

