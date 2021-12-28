package org.llvm.dwarf

import org.llvm.{LLVMObjectWrapper, api}

import scala.language.implicitConversions

abstract class DIType(val llvmDIType: api.Metadata) extends DIScope(llvmDIType) {

}

class DIBasicType(val llvmDIBasicType: api.Metadata) extends DIType(llvmDIBasicType) {

}

class DICompositeType(val llvmDICompositeType: api.Metadata) extends DIType(llvmDICompositeType) {}
class DIDerivedType(val llvmDIDerivedType: api.Metadata) extends DIType(llvmDIDerivedType) {}
class DIStringType(val llvmDIStringType: api.Metadata) extends DIType(llvmDIStringType) {}
class DISubroutineType(val llvmDISubroutineType: api.Metadata) extends DIType(llvmDISubroutineType) {}
