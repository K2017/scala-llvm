package org.llvm.dwarf

import org.bytedeco.llvm.LLVM.LLVMMetadataRef
import org.bytedeco.llvm.global.LLVM.{LLVMDebugMetadataVersion, LLVMGetMetadataKind, LLVMValueAsMetadata}
import org.llvm.dwarf.MetadataKind.MetadataKind
import org.llvm.{LLVMObjectWrapper, Module, Value}

import scala.language.implicitConversions

class Metadata(val llvmMetadata: LLVMMetadataRef) extends LLVMObjectWrapper {
  override val llvmObject: LLVMMetadataRef = llvmMetadata

  def getKind: MetadataKind = new MetadataKind(LLVMGetMetadataKind(this))
}

object Metadata {
  implicit def toLLVMMetadata(metadata: Metadata): LLVMMetadataRef = metadata.llvmMetadata
  def version: Int = LLVMDebugMetadataVersion()
  def versionAsMetadata(implicit module: Module): Metadata = Value.from(version)

  def fromValue(v: Value): Metadata = new Metadata(LLVMValueAsMetadata(v))
}

class ValueAsMetadata(override val llvmMetadata: LLVMMetadataRef) extends Metadata(llvmMetadata) {

}