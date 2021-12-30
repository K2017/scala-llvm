package org.llvm

import org.bytedeco.javacpp.{BytePointer, PointerPointer}
import org.bytedeco.llvm.LLVM.LLVMModuleRef
import org.bytedeco.llvm.global.LLVM._
import org.llvm.Linkage.Linkage
import org.llvm.ModuleFlagBehaviour.ModuleFlagBehaviour
import org.llvm.dwarf.Metadata

import scala.language.implicitConversions

class Module(val llvmModule: LLVMModuleRef, val targetMachine: Option[TargetMachine] = None) extends LLVMObjectWrapper with Disposable {
  val llvmObject: LLVMModuleRef = llvmModule
  implicit lazy val context: Context = Context.resolveContext(LLVMGetModuleContext(this))

  override def toString: String = {
    val ptr = LLVMPrintModuleToString(this)
    val str = ptr.getString
    LLVMDisposeMessage(ptr)
    str
  }

  def IRtoFile(name: String): Option[String] = {
    val errorRef = new BytePointer()
    LLVMPrintModuleToFile(this, name, errorRef) match {
      case 1 =>
        val errorStr = errorRef.getString
        LLVMDisposeMessage(errorRef)
        Some(errorStr)
      case _ => None
    }
  }

  def bitCodeToFile(name: String): Unit = {
    LLVMWriteBitcodeToFile(this, name) match {
      case 0 =>
      case code => throw BitCodeWriterException(s"BitCode writer returned non-zero exit code: $code")
    }
  }

  def toObjectFile(name: String): Option[String] = {
    targetMachine match {
      case None => Some(s"Cannot emit object code: No target machine assigned to module")
      case Some(tm) => tm.emitToFile(this, name)
    }
  }

//  def compile(optimizationLevel: Int = 3, doVerify: Boolean = true): Engine = {
//    if (doVerify) verify().map(error => throw InvalidModuleException(error))
//
//    val engineRef = new PointerByReference()
//    val errorStrRef = new PointerByReference()
//    api.tools.LLVMToolsCompileModuleWithMCJIT(engineRef, this, optimizationLevel, errorStrRef) match {
//      case 1 => {
//        val errorStr = errorStrRef.getValue.getString(0)
//        api.LLVMDisposeMessage(errorStrRef.getValue)
//        throw EngineCompilationException(errorStr)
//      }
//      case _ => new Engine(engineRef.getValue)
//    }
//  }

  def verify(errorsToStdout: Boolean = false): Option[String] = {
    val errorStrRef = new BytePointer()
    LLVMVerifyModule(this, if (errorsToStdout) 1 else 2, errorStrRef) match {
      case 1 =>
        val errorStr = errorStrRef.getString
        LLVMDisposeMessage(errorStrRef)
        Some(errorStr)
      case _ => None
    }
  }

  def createStruct(name: String, elementTypes: Seq[Type], packed: Boolean = false): StructType = {
    val struct = declareStruct(name)
    struct.setBody(elementTypes, packed)
  }

  def declareStruct(name: String): PartialStructType = {
    val llvmType = {
      LLVMStructCreateNamed(context, name)
    }
    new PartialStructType(llvmType)
  }

  def createGlobalString(str: String): GlobalVariable = new GlobalVariable(LLVMConstStringInContext(this.context, str, str.length, 0))(this)
  def createGlobalStruct(structType: StructType, initializers: Value*): GlobalVariable = new GlobalVariable(
    LLVMConstNamedStruct(structType, new PointerPointer(initializers.map(_.llvmValue): _*), initializers.length)
  )(this)
  def addGlobalVariable(typ: Type, name: String) = new GlobalVariable(LLVMAddGlobal(this, typ, name))(this)
  def setGlobalInitializer(global: GlobalVariable, init: Value): Unit = LLVMSetInitializer(global, init)
  def addGlobalWithInit(init: GlobalVariable, name: String, constant: Boolean = false, linkage: Linkage = Linkage.External): GlobalVariable = {
    val tpe = init.getType
    val global = addGlobalVariable(tpe, name)
    setGlobalInitializer(global, init)
    if (constant) {
      LLVMSetGlobalConstant(global, 1)
    }
    global.setLinkage(linkage)
    global
  }
  def addGlobalConstant(init: GlobalVariable, name: String, linkage: Linkage = Linkage.External): GlobalVariable =
    addGlobalWithInit(init, name, constant = true, linkage)


  def setSourceFile(name: String): Unit = LLVMSetSourceFileName(this, name, name.length)

  def setDataLayout(dl: TargetData): Unit = LLVMSetModuleDataLayout(this, dl)
  def getDataLayout: TargetData = new TargetData(LLVMGetModuleDataLayout(this))
  def setTargetTriple(triple: String): Unit = LLVMSetTarget(this, triple)
  def addFlag(value: Metadata, key: String, behaviour: ModuleFlagBehaviour): Unit = LLVMAddModuleFlag(this, behaviour.id, key, key.length, value)

  protected def doDispose(): Unit = LLVMDisposeModule(this)
}

object Module {
  implicit def moduleToLLVM(module: Module): LLVMModuleRef = module.llvmModule

  def create(name: String, onError: String => Unit = _ => {})(implicit context: Context): Module = {
    val moduleRef = LLVMModuleCreateWithNameInContext(name, context)
    Target.createDefault() match {
      case Left(value) =>
        val targetMachine = TargetMachine.fromTarget(value)
        val module = new Module(moduleRef, Some(targetMachine))
        module.setDataLayout(targetMachine.createDataLayout())
        module.setTargetTriple(targetMachine.triple)
        module
      case Right(value) =>
        onError(s"Error initializing TargetMachine for Module: $value")
        new Module(moduleRef)
    }
  }
}









