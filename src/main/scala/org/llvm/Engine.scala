package org.llvm

import org.bytedeco.llvm.LLVM.LLVMExecutionEngineRef
import org.bytedeco.llvm.global.LLVM.LLVMDisposeExecutionEngine

import scala.language.implicitConversions

case class InvalidModuleException(what: String) extends LLVMException(what)
case class BitCodeWriterException(what: String) extends LLVMException(what)
case class ObjectCodeWriterException(what: String) extends LLVMException(what)
case class EngineCompilationException(what: String) extends LLVMException(what)
case class InvalidFunctionException(what: String) extends LLVMException(what)
case class InvalidTypeException(what: String) extends LLVMException(what)

class Engine(val llvmEngine: LLVMExecutionEngineRef) extends Disposable {
  protected def doDispose(): Unit = LLVMDisposeExecutionEngine(this)

//  def getCompiledFunction(function: Function): CompiledFunction = {
//    val fptr = api.tools.LLVMToolsGetPointerToFunction(this, function)
//    if (fptr == null)
//      throw InvalidFunctionException(s"Cannot retrieve function ${function.name} from compiled module")
//    CompiledFunction(function, fptr, this)
//  }
}

object Engine {
  implicit def engineToLLVM(engine: Engine): LLVMExecutionEngineRef = engine.llvmEngine
}
