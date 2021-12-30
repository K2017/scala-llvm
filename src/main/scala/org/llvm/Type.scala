package org.llvm

import org.bytedeco.javacpp.{Pointer, PointerPointer}
import org.bytedeco.llvm.LLVM.LLVMTypeRef
import org.bytedeco.llvm.global.LLVM.{LLVMCountStructElementTypes, LLVMDisposeMessage, LLVMGetElementType, LLVMGetStructElementTypes, LLVMGetStructName, LLVMGetTypeContext, LLVMPointerType, LLVMPrintTypeToString, LLVMStructSetBody}

import scala.language.implicitConversions

class UnsupportedTypeException(what: String) extends LLVMException(what)

abstract class Type(val llvmType: LLVMTypeRef) extends LLVMObjectWrapper {
  val llvmObject: Pointer = llvmType

  implicit lazy val context: Context = Context.resolveContext(LLVMGetTypeContext(this))

  override def toString: String = {
    val ptr = LLVMPrintTypeToString(this)
    val str = ptr.getString
    LLVMDisposeMessage(ptr)
    str
  }

  def pointerTo: PointerType = new PointerType(LLVMPointerType(this, 0))
  def * : PointerType = pointerTo
}

abstract class PrimitiveType(llvmType: LLVMTypeRef) extends Type(llvmType) {
  val primitiveType: Manifest[_]
}

object Type {
  implicit def typeToLLVM(t: Type): t.llvmType.type = t.llvmType

  private[llvm] def resolveLLVMType(theType: LLVMTypeRef)(implicit context: Context): Type = context.resolveType(theType)
}

class VoidType(llvmType: LLVMTypeRef) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Unit] = manifest[Unit]
}

class FloatType(llvmType: LLVMTypeRef) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Float] = manifest[Float]
}

class Int32Type(llvmType: LLVMTypeRef) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Int] = manifest[Int]
}

class Int8Type(llvmType: LLVMTypeRef) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Char] = manifest[Char]
}

class Int1Type(llvmType: LLVMTypeRef) extends PrimitiveType(llvmType) {
  val primitiveType: Manifest[Boolean] = manifest[Boolean]
}

// You should *not* instantiate this class directly
private[llvm] class UnknownType(llvmType: LLVMTypeRef) extends Type(llvmType)

class StructType(llvmType: LLVMTypeRef) extends Type(llvmType) {
  def elements: Array[Type] = {
    val numElements = LLVMCountStructElementTypes(this)
    val llvmTypes = new PointerPointer[LLVMTypeRef](numElements)
    LLVMGetStructElementTypes(this, llvmTypes)
    (for (i <- 0 until numElements) yield Type.resolveLLVMType(llvmTypes.get(classOf[LLVMTypeRef], i))).toArray
  }

  def name: String = {
    val ptr = LLVMGetStructName(this)
    ptr.getString
  }
}

class PartialStructType(llvmType: LLVMTypeRef) extends StructType(llvmType) {
  def setBody(elementTypes: Seq[Type], packed: Boolean = false): StructType = {
    val typesArray = elementTypes.toArray.map {
      _.llvmType
    }
    val types = new PointerPointer[LLVMTypeRef](typesArray: _*)
    LLVMStructSetBody(this, types, typesArray.length, packed)
    this
  }
}

class PointerType(llvmType: LLVMTypeRef) extends Type(llvmType) {
  lazy val pointedType: Type = Type.resolveLLVMType(LLVMGetElementType(this))
}

class ArrayType(llvmType: LLVMTypeRef) extends Type(llvmType) {

}
