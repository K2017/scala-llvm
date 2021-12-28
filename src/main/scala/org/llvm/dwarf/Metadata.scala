package org.llvm.dwarf

import org.llvm.api.GenericObject
import org.llvm.{LLVMObjectWrapper, api}

import scala.language.implicitConversions

class Metadata(val llvmMetadata: api.Metadata) extends LLVMObjectWrapper {
  override val llvmObject: GenericObject = llvmMetadata
}

object Metadata {
  implicit def toDIMetadata(metadata: Metadata): api.Metadata = metadata.llvmMetadata
}