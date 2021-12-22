package org.llvm

import com.sun.jna.ptr.PointerByReference

import scala.language.implicitConversions

class Module(val llvmModule: api.Module) extends LLVMObjectWrapper with Disposable {
  val llvmObject: api.GenericObject = llvmModule
  implicit lazy val context: Context = Context.resolveContext(api.LLVMGetModuleContext(this))

  override def toString: String = {
    val ptr = api.LLVMPrintModuleToString(this)
    val str = ptr.getString(0)
    api.LLVMDisposeMessage(ptr)
    str
  }

  def IRtoFile(name: String): Option[String] = {
    val errorRef = new PointerByReference()
    api.LLVMPrintModuleToFile(this, name, errorRef) match {
      case 1 =>
        val errorStr = errorRef.getValue.getString(0)
        api.LLVMDisposeMessage(errorRef.getValue)
        Some(errorStr)
      case _ => None
    }
  }

  def bitCodeToFile(name: String): Unit = {
    api.LLVMWriteBitcodeToFile(this, name) match {
      case 0 =>
      case code => throw BitCodeWriterException(s"BitCode writer returned non-zero exit code: $code")
    }
  }

  def compile(optimizationLevel: Int = 3, doVerify: Boolean = true): Engine = {
    if (doVerify) verify().map(error => throw InvalidModuleException(error))

    val engineRef = new PointerByReference()
    val errorStrRef = new PointerByReference()
    api.tools.LLVMToolsCompileModuleWithMCJIT(engineRef, this, optimizationLevel, errorStrRef) match {
      case 1 => {
        val errorStr = errorStrRef.getValue.getString(0)
        api.LLVMDisposeMessage(errorStrRef.getValue)
        throw EngineCompilationException(errorStr)
      }
      case _ => new Engine(engineRef.getValue)
    }
  }

  def verify(errorsToStdout: Boolean = false): Option[String] = {
    val errorStrRef = new PointerByReference()
    api.LLVMVerifyModule(this, if (errorsToStdout) 1 else 2, errorStrRef) match {
      case 1 => {
        val errorStr = errorStrRef.getValue.getString(0)
        api.LLVMDisposeMessage(errorStrRef.getValue)
        Some(errorStr)
      }
      case _ => None
    }
  }

  def createStruct(name: String, elementTypes: Seq[Type], packed: Boolean = false): StructType = {
    val llvmType: api.Type = {
      val typesArray = elementTypes.toArray.map {
        _.llvmType
      }
      val struct = api.LLVMStructCreateNamed(context, name)
      api.LLVMStructSetBody(struct, typesArray, typesArray.length, packed)
      struct
    }
    new StructType(llvmType)
  }

  def addGlobalVariable(typ: Type, name: String) = new GlobalVariable(api.LLVMAddGlobal(this, typ, name))(this)

  def setSourceFile(name: String): Unit = api.LLVMSetSourceFileName(this, name, name.length)

  protected def doDispose(): Unit = api.LLVMDisposeModule(this)
}

object Module {
  implicit def moduleToLLVM(module: Module): api.Module = module.llvmModule

  def create(name: String)(implicit context: Context): Module = new Module(api.LLVMModuleCreateWithNameInContext(name, context))
}
