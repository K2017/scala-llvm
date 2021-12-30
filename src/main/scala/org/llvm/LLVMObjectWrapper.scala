package org.llvm

import org.bytedeco.javacpp.Pointer

trait LLVMObjectWrapper {
  val llvmObject: Pointer

  override def equals(o: Any): Boolean = o match {
    case that: LLVMObjectWrapper => llvmObject == that.llvmObject
    case _ => false
  }
}
