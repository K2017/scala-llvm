package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef
import org.bytedeco.llvm.global.LLVM._
import org.llvm.TargetData

abstract class DIType(val llvmDIType: LLVMMetadataRef) extends DIScope(llvmDIType) {

  def getSizeInBits: Long = LLVMDITypeGetSizeInBits(this)
  def getAlignInBits: Long = LLVMDITypeGetAlignInBits(this)
  def getOffsetInBits: Long = LLVMDITypeGetOffsetInBits(this)
  def asArtificial()(implicit builder: DIBuilder) = new DIDerivedType(LLVMDIBuilderCreateArtificialType(builder, this))

  def asObjPtr()(implicit builder: DIBuilder) = new DIDerivedType(LLVMDIBuilderCreateObjectPointerType(builder, this))

  def pointerTo()(implicit builder: DIBuilder, dl: TargetData): DIDerivedType = builder.createPointerType("", this)
}

class DIBasicType(val llvmDIBasicType: LLVMMetadataRef) extends DIType(llvmDIBasicType) {

}

class DICompositeType(val llvmDICompositeType: LLVMMetadataRef) extends DIType(llvmDICompositeType) {}

class DIDerivedType(val llvmDIDerivedType: LLVMMetadataRef) extends DIType(llvmDIDerivedType) {}

class DIStringType(val llvmDIStringType: LLVMMetadataRef) extends DIType(llvmDIStringType) {}

class DISubroutineType(val llvmDISubroutineType: LLVMMetadataRef) extends DIType(llvmDISubroutineType) {}
