package org.llvm.dwarf

import org.llvm.dwarf.EmissionKind.EmissionKind
import org.llvm.dwarf.Flag.Flag
import org.llvm.dwarf.encoding.BaseType.BaseType
import org.llvm.dwarf.encoding.Language.Language
import org.llvm.{Disposable, Module, api}
import org.llvm.booleanToCInt

import scala.language.implicitConversions

class DIBuilder(val llvmDIBuilder: api.DIBuilder) extends Disposable {

  def createFile(name: String, dir: String): DIFile = new DIFile(api.LLVMDIBuilderCreateFile(this, name, name.length, dir, dir.length))

  def createUnspecifiedType(name: String): DIBasicType = new DIBasicType(api.LLVMDIBuilderCreateUnspecifiedType(this, name, name.length))
  def createBasicType(name: String, sizeInBits: Int, encoding: BaseType, flags: Flag = Flag.Zero): DIBasicType = {
    new DIBasicType(api.LLVMDIBuilderCreateBasicType(this, name, name.length, sizeInBits, encoding.id, flags.id))
  }
  def createPointerType(name: String, from: DIType, sizeInBits: Int, alignInBits: Int, addressSpace: Int) = new DIDerivedType(api.LLVMDIBuilderCreatePointerType(this, from, sizeInBits, alignInBits, addressSpace, name, name.length))

  def createFunction(name: String, scope: DIScope, linkageName: String, file: DIFile, lineNo: Int, ty: DISubroutineType,
                     isLocalToUnit: Boolean, isDefinition: Boolean, scopeLine: Int, optimized: Boolean, flags: Flag = Flag.Zero): DISubprogram =
    new DISubprogram(
      api.LLVMDIBuilderCreateFunction(this, scope, name, name.length, linkageName, linkageName.length, file,
        lineNo, ty, isLocalToUnit, isDefinition, scopeLine, flags.id, optimized)
    )

  def createSubroutineType(file: DIFile, retType: DIType, params: Array[DIType], flags: Flag = Flag.Zero): DISubroutineType =
    new DISubroutineType(api.LLVMDIBuilderCreateSubroutineType(this, file.llvmDIFile, (retType +: params) map(_.llvmDIType), params.length + 1, flags.id))

  def createCompileUnit(language: Language, file: DIFile, producer: String, optimized: Boolean, compileFlags: String, runtimeVersion: Int,
                        splitName: String = "something", kind: EmissionKind = EmissionKind.FullDebug, DWOld: Int = 0,
                        splitDebugInlining: Boolean = true, debugInfoForProfiling: Boolean = false,
                        sysRoot: String = "something", sdk: String = "something"): DICompileUnit =
    new DICompileUnit(
      api.LLVMDIBuilderCreateCompileUnit(this, language.id, file, producer, producer.length, optimized,
        compileFlags, compileFlags.length, runtimeVersion, splitName, splitName.length, kind.id, DWOld,
        splitDebugInlining, debugInfoForProfiling, sysRoot, sysRoot.length, sdk, sdk.length)
    )

  def finalizeBuilder(): Unit = api.LLVMDIBuilderFinalize(this)
  override protected def doDispose(): Unit = api.LLVMDisposeDIBuilder(this)
}

object DIBuilder {
  implicit def builderToLLVM(builder: DIBuilder): api.DIBuilder = builder.llvmDIBuilder
  def create()(implicit module: Module) = new DIBuilder(api.LLVMCreateDIBuilder(module.llvmModule))
}
