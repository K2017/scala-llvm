package org.llvm.dwarf

import org.bytedeco.javacpp.PointerPointer
import org.bytedeco.llvm.LLVM.LLVMDIBuilderRef
import org.bytedeco.llvm.global.LLVM._
import org.llvm.dwarf.EmissionKind.EmissionKind
import org.llvm.dwarf.Flag.Flag
import org.llvm.dwarf.encoding.BaseType.BaseType
import org.llvm.dwarf.encoding.Language.Language
import org.llvm.dwarf.encoding.Tag.Tag
import org.llvm.{BasicBlock, Context, Disposable, Instruction, Module, StructType, TargetData, Type, Value, booleanToCInt}

import scala.language.implicitConversions

class DIBuilder(val llvmDIBuilder: LLVMDIBuilderRef, implicit val module: Module) extends Disposable {
  private implicit val context: Context = module.context

  def createFile(name: String, dir: String): DIFile = new DIFile(LLVMDIBuilderCreateFile(this, name, name.length, dir, dir.length))

  def createUnspecifiedType(name: String): DIBasicType = new DIBasicType(LLVMDIBuilderCreateUnspecifiedType(this, name, name.length))

  def createBasicType(name: String, sizeInBits: Int, encoding: BaseType, flags: Flag = Flag.Zero): DIBasicType = {
    new DIBasicType(LLVMDIBuilderCreateBasicType(this, name, name.length, sizeInBits, encoding.id, flags.id))
  }

  def createPointerType(name: String, from: DIType)(implicit dl: TargetData): DIDerivedType = {
    val sizeInBits = dl.getPtrSizeInBits
    new DIDerivedType(LLVMDIBuilderCreatePointerType(this, from, sizeInBits, 0, 0, name, name.length))
  }

  def createNullType() = new DIBasicType(LLVMDIBuilderCreateNullPtrType(this))

  def createReplaceableForwardDeclaration(name: String, scope: DIScope, file: DIFile, tag: Tag, line: Int) =
    new DICompositeType(LLVMDIBuilderCreateReplaceableCompositeType(this, tag.id, name, name.length, scope, file, line, 0, 0, 0, Flag.Zero.id, name, name.length))

  def createClassType(name: String, tp: StructType, vTable: Option[DIType], scope: DIScope, file: DIFile, line: Int, flags: Flag, elements: Array[DINode])(implicit dl: TargetData): DICompositeType = {
    val sizeInBits = dl.getTypeSizeInBits(tp)
    new DICompositeType(
      LLVMDIBuilderCreateClassType(this, null, name, name.length, file, line, sizeInBits, 0, 0, flags.id, null, new PointerPointer(elements.map(_.llvmMetadata): _*), elements.length, vTable match { case Some(v) => v; case None => null }, null, name, name.length)
    )
  }

  def createMemberType(name: String, tp: Type, diType: DIType, idx: Int, parent: StructType, line: Int, scope: DIScope, file: DIFile, flags: Flag = Flag.Zero)(implicit dl: TargetData): DIDerivedType = new DIDerivedType({
    val sizeInBits = dl.getTypeSizeInBits(tp)
    val offsetInBits = dl.getOffsetOfElement(parent, idx) * 8
    LLVMDIBuilderCreateMemberType(this, scope, name, name.length, file, line, sizeInBits, 0, offsetInBits, flags.id, diType)
  })

  def createDerivedClassType(name: String, tp: StructType, vTable: Option[DIType], scope: DIScope, file: DIFile, derivedFrom: DIType, line: Int, flags: Flag, elements: Array[DINode])(implicit dl: TargetData): DICompositeType = {
    val sizeInBits = dl.getTypeSizeInBits(tp)
    new DICompositeType(
      LLVMDIBuilderCreateClassType(this, scope, name, name.length, file, line, sizeInBits, 0, 0, flags.id, derivedFrom, new PointerPointer(elements.map(_.llvmMetadata): _*), elements.length, vTable match { case Some(v) => v; case None => null }, null, name, name.length)
    )
  }

  def createStructType(name: String, tp: StructType, elements: Array[DINode], scope: DIScope, file: DIFile, line: Int)(implicit dl: TargetData) = new DIDerivedType({
    val sizeInBits = dl.getTypeSizeInBits(tp)
    LLVMDIBuilderCreateStructType(this, scope, name, name.length, file, line, sizeInBits, 0, Flag.Zero.id, null, new PointerPointer(elements.map(_.llvmMetadata): _*), elements.length, 0, null, name, name.length)
  })

  def createInheritance(tp: DIType, base: DIType, flags: Flag = Flag.Zero) = new DIDerivedType(
    LLVMDIBuilderCreateInheritance(this, tp, base, 0, 0, flags.id)
  )

  def createParameterVariable(name: String, tp: DIType, argIdx: Int, line: Int, file: DIFile, scope: DILocalScope, flags: Flag = Flag.Zero) = new DILocalVariable(
    LLVMDIBuilderCreateParameterVariable(this, scope, name, name.length, argIdx, file, line, tp, true, flags.id)
  )

  def createLocalVariable(name: String, tp: DIType, line: Int, file: DIFile, scope: DILocalScope) = new DILocalVariable(
    LLVMDIBuilderCreateParameterVariable(this, scope, name, name.length, 0, file, line, tp, true, Flag.Zero.id)
  )

  def createFunction(name: String, scope: DIScope, linkageName: String, file: DIFile, lineNo: Int, ty: DISubroutineType,
                     isLocalToUnit: Boolean, isDefinition: Boolean, scopeLine: Int, optimized: Boolean, flags: Flag = Flag.Zero): DISubprogram =
    new DISubprogram(
      LLVMDIBuilderCreateFunction(this, scope, name, name.length, linkageName, linkageName.length, file,
        lineNo, ty, isLocalToUnit, isDefinition, scopeLine, flags.id, optimized)
    )

  def createSubroutineType(file: DIFile, retType: DIType, params: Array[DIType] = Array(), flags: Flag = Flag.Zero): DISubroutineType =
    new DISubroutineType(
      LLVMDIBuilderCreateSubroutineType(this, file.llvmDIFile, new PointerPointer((retType +: params) map (_.llvmDIType): _*), params.length + 1, flags.id)
    )

  def createCompileUnit(language: Language, file: DIFile, producer: String, optimized: Boolean, compileFlags: String, runtimeVersion: Int,
                        splitName: String = "", kind: EmissionKind = EmissionKind.FullDebug, DWOld: Int = 0,
                        splitDebugInlining: Boolean = true, debugInfoForProfiling: Boolean = false,
                        sysRoot: String = "", sdk: String = ""): DICompileUnit =
    new DICompileUnit(
      LLVMDIBuilderCreateCompileUnit(this, language.id - 1, file, producer, producer.length, optimized,
        compileFlags, compileFlags.length, runtimeVersion, splitName, splitName.length, kind.id, DWOld,
        splitDebugInlining, debugInfoForProfiling, sysRoot, sysRoot.length, sdk, sdk.length)
    )

  def createLocation(line: Int, column: Int, scope: DIScope, inlinedAt: Option[DIScope] = None) =
    new DILocation(LLVMDIBuilderCreateDebugLocation(context, line, column, scope, inlinedAt match {
      case None => null
      case Some(value) => value
    }))

  def createExpression(ops: Long*): DIExpression = new DIExpression(LLVMDIBuilderCreateExpression(this, ops.toArray, ops.length))

  def createLexicalBlock(parent: DIScope, file: DIFile, line: Int, column: Int) = new DILexicalBlock(
    LLVMDIBuilderCreateLexicalBlock(this, parent, file, line, column)
  )

  def insertDeclare(alloc: Value, dbgInfo: Metadata, loc: DILocation, instr: Instruction, expr: DIExpression): Instruction =
    new Instruction(LLVMDIBuilderInsertDeclareBefore(this, alloc, dbgInfo, expr, loc, instr))

  def insertDeclare(alloc: Value, dbgInfo: Metadata, loc: DILocation, instr: BasicBlock, expr: DIExpression): Instruction =
    new Instruction(LLVMDIBuilderInsertDeclareAtEnd(this, alloc, dbgInfo, expr, loc, instr))

  def insertValue(value: Value, dbgInfo: Metadata, loc: DILocation, instr: Instruction, expr: DIExpression) =
    new Instruction(LLVMDIBuilderInsertDbgValueBefore(this, value, dbgInfo, expr, loc, instr))

  def insertValue(value: Value, dbgInfo: Metadata, loc: DILocation, instr: BasicBlock, expr: DIExpression) =
    new Instruction(LLVMDIBuilderInsertDbgValueAtEnd(this, value, dbgInfo, expr, loc, instr))

  def finalizeBuilder(): Unit = LLVMDIBuilderFinalize(this)

  def replaceAllUses(of: Metadata, replacement: Metadata): Unit = LLVMMetadataReplaceAllUsesWith(of, replacement)

  override protected def doDispose(): Unit = LLVMDisposeDIBuilder(this)
}

object DIBuilder {
  implicit def builderToLLVM(builder: DIBuilder): LLVMDIBuilderRef = builder.llvmDIBuilder

  def create()(implicit module: Module) = new DIBuilder(LLVMCreateDIBuilder(module.llvmModule), module)
}