package org.llvm.dwarf

import org.llvm.{LLVMObjectWrapper, api}

class DIScope(val scope: api.Metadata) extends DINode(scope) {
}

class DILocalScope(val llvmDILocalScope: api.Metadata) extends DIScope(llvmDILocalScope)
