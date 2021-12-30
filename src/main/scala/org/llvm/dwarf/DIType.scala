package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef

abstract class DIType(val llvmDIType: LLVMMetadataRef) extends DIScope(llvmDIType) {

}

class DIBasicType(val llvmDIBasicType: LLVMMetadataRef) extends DIType(llvmDIBasicType) {

}

class DICompositeType(val llvmDICompositeType: LLVMMetadataRef) extends DIType(llvmDICompositeType) {}

class DIDerivedType(val llvmDIDerivedType: LLVMMetadataRef) extends DIType(llvmDIDerivedType) {}

class DIStringType(val llvmDIStringType: LLVMMetadataRef) extends DIType(llvmDIStringType) {}

class DISubroutineType(val llvmDISubroutineType: LLVMMetadataRef) extends DIType(llvmDISubroutineType) {}
