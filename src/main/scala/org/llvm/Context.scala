package org.llvm

import org.bytedeco.javacpp.Pointer
import org.bytedeco.llvm.LLVM.{LLVMContextRef, LLVMTypeRef}
import org.bytedeco.llvm.global.LLVM._

import scala.collection.mutable
import scala.language.{implicitConversions, postfixOps}

case class InvalidContextException(what: String) extends LLVMException(what)

class Context(val llvmContext: LLVMContextRef) extends LLVMObjectWrapper with Disposable {
  override val llvmObject: Pointer = llvmContext
  /** Basic types */
  val context: Context = this
  private[llvm] val typeMap: mutable.Map[LLVMTypeRef, Type] = mutable.Map.empty[LLVMTypeRef, Type]

  def doDispose(): Unit = {
    LLVMContextDispose(this)
    Context.unregisterContext(this)
  }

  def resolveType(theType: LLVMTypeRef): Type = {
    typeMap.getOrElseUpdate(theType, {
      LLVMGetTypeKind(theType) match {
        case LLVMIntegerTypeKind => LLVMGetIntTypeWidth(theType) match {
          case 32 => new Int32Type(theType)
          case 8 => new Int8Type(theType)
          case 1 => new Int1Type(theType)
        }
        case LLVMVoidTypeKind => new VoidType(theType)
        case LLVMFloatTypeKind => new FloatType(theType)
        case LLVMPointerTypeKind => new PointerType(theType)
        case LLVMStructTypeKind => new StructType(theType)
        case LLVMFunctionTypeKind => new FunctionType(theType)
        case LLVMArrayTypeKind => new ArrayType(theType)
        case _ =>
          val unknownType = new UnknownType(theType)
          throw new UnsupportedTypeException(s"Cannot resolve type '$unknownType' in context")
      }
    })
  }


  object Types {
    lazy val void: VoidType = resolveType(LLVMVoidTypeInContext(context)).asInstanceOf[VoidType]
    lazy val i32: Int32Type = resolveType(LLVMInt32TypeInContext(context)).asInstanceOf[Int32Type]
    lazy val i1: Int1Type = resolveType(LLVMInt1TypeInContext(context)).asInstanceOf[Int1Type]
    lazy val i8: Int8Type = resolveType(LLVMInt8TypeInContext(context)).asInstanceOf[Int8Type]
    lazy val char: Int8Type = this.i8
    lazy val float: FloatType = resolveType(LLVMFloatTypeInContext(context)).asInstanceOf[FloatType]
    lazy val asciistring: PointerType = this.char*
  }

}

object Context {
  implicit def contextToLLVM(context: Context): LLVMContextRef = context.llvmContext

  val contexts: mutable.Map[LLVMContextRef, Context] = mutable.Map.empty
  private var llvmInitialized = false

  /**
   * Create a new LLVM Context and optionally initialize LLVM components if they haven't been already
   * @return
   */
  def create(): Context = {
    initLLVM()
    val context = new Context(LLVMContextCreate())
    contexts += (context.llvmContext -> context)
    context
  }

  private def initLLVM(): Unit = {
    if (llvmInitialized) return
    LLVMInitializeAllTargetInfos()
    LLVMInitializeAllTargets()
    LLVMInitializeAllTargetMCs()
    LLVMInitializeAllAsmParsers()
    LLVMInitializeAllAsmPrinters()
    llvmInitialized = true
  }

  def resolveContext(context: LLVMContextRef): Context = {
    contexts.getOrElse(context, {
      throw InvalidContextException("Could not resolve context")
    })
  }

  private def unregisterContext(context: Context): Unit = {
    contexts -= context.llvmContext
  }
}

