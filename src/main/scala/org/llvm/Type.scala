package org.llvm

import scala.language.implicitConversions

class UnsupportedTypeException(what: String) extends LLVMException(what)

abstract class Type(val llvmType: api.Type) extends LLVMObjectWrapper {
  val llvmObject: api.GenericObject = llvmType

  implicit lazy val context: Context = Context.resolveContext(api.LLVMGetTypeContext(this))

  override def toString: String = {
    val ptr = api.LLVMPrintTypeToString(this)
    val str = ptr.getString(0)
    api.LLVMDisposeMessage(ptr)
    str
  }

  def pointerTo: PointerType = new PointerType(api.LLVMPointerType(this, 0))
}

abstract class PrimitiveType(llvmType: api.Type) extends Type(llvmType) {
  val primitiveType: Manifest[_]
}

object Type {
  implicit def typeToLLVM(t: Type): api.Type = t.llvmType

  private[llvm] def resolveLLVMType(theType: api.Type)(implicit context: Context): Type = context.resolveType(theType)
}

class VoidType(llvmType: api.Type) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Unit] = manifest[Unit]
}

class FloatType(llvmType: api.Type) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Float] = manifest[Float]
}

class Int32Type(llvmType: api.Type) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Int] = manifest[Int]
}

// You should *not* instantiate this class directly
private[llvm] class UnknownType(llvmType: api.Type) extends Type(llvmType)

class StructType(llvmType: api.Type) extends Type(llvmType) {
  def elements: Array[Type] = {
    val numElements = api.LLVMCountStructElementTypes(this)
    val llvmTypes = new Array[api.Type](numElements)
    api.LLVMGetStructElementTypes(this, llvmTypes)
    llvmTypes.map { Type.resolveLLVMType }
  }

  def name: String = api.LLVMGetStructName(this)
}

class PointerType(llvmType: api.Type) extends Type(llvmType) {
  lazy val pointedType: Type = Type.resolveLLVMType(api.LLVMGetElementType(this))
}
