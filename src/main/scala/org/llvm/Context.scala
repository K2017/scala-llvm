package org.llvm

import scala.collection.mutable
import scala.language.implicitConversions

case class InvalidContextException(what: String) extends LLVMException(what)

class Context(val llvmContext: api.Context) extends LLVMObjectWrapper with Disposable {
  val llvmObject: api.GenericObject = llvmContext
  /** Basic types */
  val context: Context = this
  private[llvm] val typeMap: mutable.Map[api.Type, Type] = mutable.Map.empty[api.Type, Type]

  def doDispose(): Unit = {
    api.LLVMContextDispose(this)
    Context.unregisterContext(this)
  }

  def resolveType(theType: api.Type): Type = {
    typeMap.getOrElseUpdate(theType, {
      api.LLVMGetTypeKind(theType) match {
        case api.LLVMIntegerTypeKind => api.LLVMGetIntTypeWidth(theType) match {
          case 32 => new Int32Type(theType)
          case 8 => new Int8Type(theType)
          case 1 => new Int1Type(theType)
        }
        case api.LLVMVoidTypeKind => new VoidType(theType)
        case api.LLVMFloatTypeKind => new FloatType(theType)
        case api.LLVMPointerTypeKind => new PointerType(theType)
        case api.LLVMStructTypeKind => new StructType(theType)
        case api.LLVMFunctionTypeKind => new FunctionType(theType)
        case api.LLVMArrayTypeKind => new ArrayType(theType)
        case _ =>
          val unknownType = new UnknownType(theType)
          throw new UnsupportedTypeException(s"Cannot resolve type '$unknownType' in context")
      }
    })
  }


  object Types {
    lazy val void: VoidType = resolveType(api.LLVMVoidTypeInContext(context)).asInstanceOf[VoidType]
    lazy val i32: Int32Type = resolveType(api.LLVMInt32TypeInContext(context)).asInstanceOf[Int32Type]
    lazy val i1: Int1Type = resolveType(api.LLVMInt1TypeInContext(context)).asInstanceOf[Int1Type]
    lazy val i8: Int8Type = resolveType(api.LLVMInt8TypeInContext(context)).asInstanceOf[Int8Type]
    lazy val char: Int8Type = this.i8
    lazy val float: FloatType = resolveType(api.LLVMFloatTypeInContext(context)).asInstanceOf[FloatType]
    lazy val asciistring: PointerType = this.char.pointerTo
  }
}

object Context {
  implicit def contextToLLVM(context: Context): api.Context = context.llvmContext

  val contexts: mutable.Map[api.Context, Context] = mutable.Map.empty

  def create(): Context = {
    val context = new Context(api.LLVMContextCreate())
    contexts += (context.llvmContext -> context)
    context
  }

  def resolveContext(context: api.Context): Context = {
    contexts.getOrElse(context, {
      throw InvalidContextException("Could not resolve context")
    })
  }

  private def unregisterContext(context: Context): Unit = {
    contexts -= context.llvmContext
  }
}

