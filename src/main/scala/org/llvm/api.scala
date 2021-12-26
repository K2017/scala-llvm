package org.llvm

import com.sun.jna.ptr.PointerByReference
import com.sun.jna.{Library, Native, Pointer}

private[llvm] object api {
  type GenericObject = Pointer
  type Context = Pointer
  type Module = Pointer
  type Value = Pointer
  type Type = Pointer
  type Builder = Pointer
  type FunctionType = Pointer
  type BasicBlock = Pointer
  type ExecutionEngine = Pointer
  type PassManager = Pointer
  type PassBuilderOptions = Pointer

  val libname = "LLVM-13"

  Native.register(libname)
  // Enums
  val LLVMIntEq = 32
  val LLVMIntNE = 33
  val LLVMIntSGT = 38
  val LLVMIntSGE = 39
  val LLVMIntSLT = 40
  val LLVMIntSLE = 41
  val LLVMRealOEQ = 1
  val LLVMRealOGT = 2
  val LLVMRealOGE = 3
  val LLVMRealOLT = 4
  val LLVMRealOLE = 5
  val LLVMRealONE = 6
  val LLVMRealORD = 7
  val LLVMVoidTypeKind = 0
  val LLVMHalfTypeKind = 1
  val LLVMFloatTypeKind = 2
  val LLVMDoubleTypeKind = 3
  val LLVMX86_FP80TypeKind = 4
  val LLVMFP128TypeKind = 5
  val LLVMPPC_FP128TypeKind = 6
  val LLVMLabelTypeKind = 7
  val LLVMIntegerTypeKind = 8
  val LLVMFunctionTypeKind = 9
  val LLVMStructTypeKind = 10
  val LLVMArrayTypeKind = 11
  val LLVMPointerTypeKind = 12
  // This has the functions that cannot be loaded using the @native method (for example, functions
  // that accept arrays as input parameters)
  private val nonNative = Native.load(libname, classOf[NonNativeApi])

  // Context
  @native def LLVMContextCreate(): api.Context
  @native def LLVMContextDispose(context: api.Context): Unit
  @native def LLVMGetTypeContext(typ: api.Type): api.Context
  @native def LLVMConstStringInContext(context: api.Context, str: String, len: Int, dontNullTerminate: Int): api.Value
  def LLVMConstStructInContext: (api.Context, Array[api.Value], Int, Boolean) => api.Value = nonNative.LLVMConstStructInContext
  def LLVMConstNamedStruct: (api.Type, Array[api.Value], Int) => api.Value = nonNative.LLVMConstNamedStruct
  @native def LLVMSetInitializer(global: api.Value, init: api.Value): Unit
  @native def LLVMSetGlobalConstant(global: api.Value, constant: Int): Unit

  // Module
  @native def LLVMModuleCreateWithNameInContext(name: String, context: api.Context): api.Module
  @native def LLVMDumpModule(module: api.Module): Unit
  @native def LLVMDisposeModule(module: api.Module): Unit
  @native def LLVMVerifyModule(module: api.Module, action: Int, errorRef: PointerByReference): Int
  @native def LLVMPrintModuleToString(module: api.Module): Pointer
  @native def LLVMPrintModuleToFile(module: api.Module, filename: String, error: PointerByReference): Int
  @native def LLVMWriteBitcodeToFile(module: api.Module, path: String): Int
  @native def LLVMDisposeExecutionEngine(engine: api.ExecutionEngine): Unit
  @native def LLVMAddGlobal(module: api.Module, typ: api.Type, name: String): api.Value
  @native def LLVMGetModuleContext(module: api.Module): api.Context
  @native def LLVMGetGlobalContext(): api.Context
  @native def LLVMSetSourceFileName(module: api.Module, name: String, len: Int): Unit


  // Pass Builder
  @native def LLVMCreatePassBuilderOptions(): api.PassBuilderOptions
  @native def LLVMDisposePassBuilderOptions(options: api.PassBuilderOptions): Unit
  @native def LLVMPassBuilderOptionsSetMergeFunctions(options: api.PassBuilderOptions, merge: Int): Unit
  @native def LLVMPassBuilderOptionsSetVerifyEach(options: api.PassBuilderOptions, verify: Int): Unit
  @native def LLVMRunPasses(module: api.Module, passes: String, tm: Pointer, options: api.PassBuilderOptions): Pointer
  @native def LLVMGetErrorMessage(err: Pointer): Pointer



  // Pass Manager
  @native def LLVMCreatePassManager(): api.PassManager
  @native def LLVMCreateFunctionPassManagerForModule(module: api.Module): api.PassManager
  @native def LLVMRunPassManager(manager: api.PassManager, module: api.Module): Int
  @native def LLVMDisposePassManager(manager: api.PassManager): Unit

  // Transformations {

  //  Utility Passes
  @native def LLVMAddPromoteMemoryToRegisterPass(manager: api.PassManager): Unit
//  @native def LLVMAddAddDiscriminatorsPass(manager: api.PassManager): Unit
  @native def LLVMAddLowerSwitchPass(manager: api.PassManager): Unit


  //  Scalar Passes {
  @native def LLVMAddAggressiveDCEPass(manager: api.PassManager): Unit
  @native def LLVMAddAlignmentFromAssumptionsPass(manager: api.PassManager): Unit
  @native def LLVMAddBasicAliasAnalysisPass(manager: api.PassManager): Unit
  @native def LLVMAddBitTrackingDCEPass(manager: api.PassManager): Unit
  @native def LLVMAddCFGSimplificationPass(manager: api.PassManager): Unit
  @native def LLVMAddCorrelatedValuePropagationPass(manager: api.PassManager): Unit
  //  @native def LLVMAddDCEPass(manager: api.PassManager): Unit
  @native def LLVMAddDeadStoreEliminationPass(manager: api.PassManager): Unit
  @native def LLVMAddDemoteMemoryToRegisterPass(manager: api.PassManager): Unit
  @native def LLVMAddEarlyCSEMemSSAPass(manager: api.PassManager): Unit
  @native def LLVMAddEarlyCSEPass(manager: api.PassManager): Unit
  @native def LLVMAddGVNPass(manager: api.PassManager): Unit
  @native def LLVMAddIndVarSimplifyPass(manager: api.PassManager): Unit
  @native def LLVMAddInstructionCombiningPass(manager: api.PassManager): Unit
//  @native def LLVMAddInstructionSimplifyPass(manager: api.PassManager): Unit
  @native def LLVMAddJumpThreadingPass(manager: api.PassManager): Unit

  //    Loop Optimizations {
  @native def LLVMAddLICMPass(manager: api.PassManager): Unit
  @native def LLVMAddLoopDeletionPass(manager: api.PassManager): Unit
  @native def LLVMAddLoopIdiomPass(manager: api.PassManager): Unit
  @native def LLVMAddLoopRerollPass(manager: api.PassManager): Unit
  @native def LLVMAddLoopRotatePass(manager: api.PassManager): Unit
  @native def LLVMAddLoopUnrollAndJamPass(manager: api.PassManager): Unit
  @native def LLVMAddLoopUnrollPass(manager: api.PassManager): Unit
  @native def LLVMAddLoopUnswitchPass(manager: api.PassManager): Unit
  //    } Loop Optimizations

//  @native def LLVMAddLowerAtomicPass(manager: api.PassManager): Unit
//  @native def LLVMAddLowerConstantIntrinsicsPass(manager: api.PassManager): Unit
  @native def LLVMAddLowerExpectIntrinsicPass(manager: api.PassManager): Unit
  @native def LLVMAddMemCpyOptPass(manager: api.PassManager): Unit
  @native def LLVMAddMergedLoadStoreMotionPass(manager: api.PassManager): Unit
  @native def LLVMAddNewGVNPass(manager: api.PassManager): Unit
  @native def LLVMAddPartiallyInlineLibCallsPass(manager: api.PassManager): Unit
  @native def LLVMAddReassociatePass(manager: api.PassManager): Unit
  @native def LLVMAddScalarizerPass(manager: api.PassManager): Unit
  @native def LLVMAddScalarReplAggregatesPass(manager: api.PassManager): Unit
  @native def LLVMAddScalarReplAggregatesPassSSA(manager: api.PassManager): Unit
  @native def LLVMAddScalarReplAggregatesPassWithThreshold(manager: api.PassManager, threshold: Int): Unit
  @native def LLVMAddSCCPPass(manager: api.PassManager): Unit
  @native def LLVMAddScopedNoAliasAAPass(manager: api.PassManager): Unit
  @native def LLVMAddSimplifyLibCallsPass(manager: api.PassManager): Unit
  @native def LLVMAddTailCallEliminationPass(manager: api.PassManager): Unit
  @native def LLVMAddTypeBasedAliasAnalysisPass(manager: api.PassManager): Unit
//  @native def LLVMAddUnifyFunctionExitNodesPass(manager: api.PassManager): Unit
  @native def LLVMAddVerifierPass(manager: api.PassManager): Unit
  //  } Scalar Passes

  //  Interprocedural Passes {
  @native def LLVMAddAlwaysInlinerPass(manager: api.PassManager): Unit
  @native def LLVMAddArgumentPromotionPass(manager: api.PassManager): Unit
  @native def LLVMAddCalledValuePropagationPass(manager: api.PassManager): Unit
  @native def LLVMAddConstantMergePass(manager: api.PassManager): Unit
  @native def LLVMAddDeadArgEliminationPass(manager: api.PassManager): Unit
  @native def LLVMAddFunctionAttrsPass(manager: api.PassManager): Unit
  @native def LLVMAddFunctionInliningPass(manager: api.PassManager): Unit
  @native def LLVMAddGlobalDCEPass(manager: api.PassManager): Unit
  @native def LLVMAddGlobalOptimizerPass(manager: api.PassManager): Unit
  @native def LLVMAddInternalizePass(manager: api.PassManager): Unit
  @native def LLVMAddIPSCCPPass(manager: api.PassManager): Unit
//  @native def LLVMAddMergeFunctionsPass(manager: api.PassManager): Unit
  @native def LLVMAddPruneEHPass(manager: api.PassManager): Unit
  @native def LLVMAddStripDeadPrototypesPass(manager: api.PassManager): Unit
  @native def LLVMAddStripSymbolsPass(manager: api.PassManager): Unit
  //  } Interprocedural Passes

  // } Transformations

  // Builder {
  @native def LLVMCreateBuilderInContext(context: api.Context): api.Builder
  @native def LLVMDisposeBuilder(builder: api.Builder): Unit
  @native def LLVMBuildRet(builder: api.Builder, value: api.Value): api.Value
  @native def LLVMBuildRetVoid(builder: api.Builder): api.Value
  def LLVMBuildCall: (api.Builder, api.Value, Array[api.Value], Int, String) => api.Value = nonNative.LLVMBuildCall
  @native def LLVMBuildGlobalStringPtr(builder: api.Builder, s: String, name: String): api.Value

  //  Arithmetic {
  @native def LLVMBuildAdd(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildFAdd(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildSub(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildFSub(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildMul(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildFMul(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildUDiv(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildSDiv(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildFDiv(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildAnd(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildOr(builder: api.Builder, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildNot(builder: api.Builder, v: api.Value, name: String): api.Value
  //  } Arithmetic

  @native def LLVMBuildICmp(builder: api.Builder, predicate: Int, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildFCmp(builder: api.Builder, predicate: Int, lhs: api.Value, rhs: api.Value, name: String): api.Value
  @native def LLVMBuildPhi(builder: api.Builder, phiType: api.Type, name: String): api.Value
  @native def LLVMBuildBr(builder: api.Builder, dest: api.BasicBlock): api.Value
  @native def LLVMBuildCondBr(builder: api.Builder, cond: api.Value, thn: api.BasicBlock, otherwise: api.BasicBlock): api.Value
  @native def LLVMBuildSelect(builder: api.Builder, iff: api.Value, thn: api.Value, otherwise: api.Value, name: String): api.Value
  @native def LLVMBuildLoad(builder: api.Builder, pointerVal: api.Value, name: String): api.Value
  @native def LLVMBuildStore(builder: api.Builder, value: api.Value, pointerVal: api.Value): api.Value
  @native def LLVMBuildMalloc(builder: api.Builder, typ: api.Type, name: String): api.Value
  @native def LLVMBuildArrayMalloc(builder: api.Builder, typ: api.Type, v: api.Value, name: String): api.Value
  @native def LLVMBuildAlloca(builder: api.Builder, typ: api.Type, name: String): api.Value
  @native def LLVMBuildArrayAlloca(builder: api.Builder, typ: api.Type, v: api.Value, name: String): api.Value
  @native def LLVMBuildFree(builder: api.Builder, ptr: api.Value): api.Value
  @native def LLVMBuildIsNull(builder: api.Builder, ptr: api.Value, name: String): api.Value
  @native def LLVMBuildIsNotNull(builder: api.Builder, ptr: api.Value, name: String): api.Value
  @native def LLVMGetInsertBlock(builder: api.Builder): api.BasicBlock
  @native def LLVMBuildStructGEP(builder: api.Builder, ptr: api.Value, idx: Int, name: String): api.Value
  @native def LLVMBuildBitCast(builder: api.Builder, v: api.Value, destTyp: api.Type, name: String): api.Value
  @native def LLVMBuildPtrToInt(builder: api.Builder, v: api.Value, destTyp: api.Type, name: String): api.Value
  @native def LLVMBuildIntToPtr(builder: api.Builder, v: api.Value, destTyp: api.Type, name: String): api.Value
  def LLVMBuildGEP: (Builder, Value, Array[Value], Int, String) => Value = nonNative.LLVMBuildGEP
  def LLVMBuildGEP2: (Builder, Type, Value, Array[Value], Int, String) => Value = nonNative.LLVMBuildGEP2
  def LLVMBuildInBoundsGEP: (Builder, Type, Value, Array[Value], Int, String) => Value = nonNative.LLVMBuildInBoundsGEP2

  //  Actions
  @native def LLVMAppendBasicBlockInContext(context: api.Context, function: api.Value, name: String): api.BasicBlock
  @native def LLVMPositionBuilderAtEnd(builder: api.Builder, block: api.BasicBlock): Unit
  // } Builder

  // Functions
  def LLVMFunctionType: (Type, Array[Type], Int, Boolean) => FunctionType = nonNative.LLVMFunctionType
  @native def LLVMSetFunctionCallConv(function: api.Value, conv: Int): Unit
  @native def LLVMGetFunctionCallConv(function: api.Value): Int
  @native def LLVMAddFunction(module: api.Module, name: String, funcType: api.Type): api.Value
  @native def LLVMDeleteFunction(func: api.Value): Unit
  @native def LLVMGetParam(function: api.Value, index: Int): api.Value
  @native def LLVMGetReturnType(functionType: api.Type): api.Type
  @native def LLVMCountParamTypes(functionType: api.Type): Int
  def LLVMGetParamTypes: (Type, Array[Type]) => Unit = nonNative.LLVMGetParamTypes

  // Types
  @native def LLVMVoidTypeInContext(context: api.Context): api.Type
  @native def LLVMInt1TypeInContext(context: api.Context): api.Type
  @native def LLVMInt8TypeInContext(context: api.Context): api.Type
  @native def LLVMInt16TypeInContext(context: api.Context): api.Type
  @native def LLVMInt32TypeInContext(context: api.Context): api.Type
  @native def LLVMInt64TypeInContext(context: api.Context): api.Type
  @native def LLVMGetIntTypeWidth(intType: api.Type): Int
  @native def LLVMFloatTypeInContext(context: api.Context): api.Type
  @native def LLVMDoubleTypeInContext(context: api.Context): api.Type
  @native def LLVMTypeOf(value: api.Value): api.Type
  @native def LLVMPointerType(elementType: api.Type, addressSpace: Int): api.Type
  @native def LLVMGetTypeKind(typ: api.Type): Int
  @native def LLVMPrintTypeToString(module: api.Type): Pointer
  @native def LLVMGetElementType(typ: api.Type): api.Type

  // Structs
  @native def LLVMStructCreateNamed(context: api.Context, name: String): api.Type
  def LLVMStructSetBody: (Type, Array[Type], Int, Boolean) => Unit = nonNative.LLVMStructSetBody
  @native def LLVMCountStructElementTypes(struct: api.Type): Int
  def LLVMGetStructElementTypes: (Type, Array[Type]) => Unit = nonNative.LLVMGetStructElementTypes
  @native def LLVMGetStructName(struct: api.Type): String

  // Constants
  @native def LLVMConstInt(intType: api.Type, value: Long, signExtend: Int): api.Value
  @native def LLVMConstReal(realType: api.Type, value: Double): api.Value
  @native def LLVMConstNull(typ: api.Type): api.Value

  // Values
  @native def LLVMSetValueName(value: api.Value, name: String): Unit
  @native def LLVMPrintValueToString(module: api.Value): Pointer
  @native def LLVMGetValueName(value: api.Value): String
  @native def LLVMSetLinkage(value: api.Value, linkage: Int): Unit
  @native def LLVMSetVisibility(value: api.Value, vis: Int): Unit

  // Basic block
  @native def LLVMGetBasicBlockTerminator(block: api.BasicBlock): api.Value
  @native def LLVMGetBasicBlockParent(block: api.BasicBlock): api.Value

  // Misc
  @native def LLVMDisposeMessage(message: Pointer): Unit
  def LLVMAddIncoming: (Value, Array[Value], Array[BasicBlock], Int) => Unit = nonNative.LLVMAddIncoming

  // LLVM tools library, used to overcome some limitations of the C api
  object tools {
    Native.register("LLVMTools")

    type InsertPoint = Pointer

    @native def LLVMToolsInitializeAll(): Unit
    @native def LLVMToolsCompileModuleWithMCJIT(outEngineRef: PointerByReference, module: api.Module, optimizationLevel: Int, errorRef: PointerByReference): Int
    @native def LLVMToolsGetPointerToFunction(engine: api.ExecutionEngine, function: api.Value): Pointer
    @native def LLVMSaveInsertPoint(builderRef: Builder): InsertPoint
    @native def LLVMRestoreInsertPoint(builderRef: Builder, ip: InsertPoint): Unit
    @native def LLVMDisposeInsertPoint(ip: InsertPoint): Unit

    @native def LLVMToolsExecute_L_L_Function(fptr: Pointer, p1: Long): Long
    @native def LLVMToolsExecute_I_I_Function(fptr: Pointer, p1: Int): Int
    @native def LLVMToolsExecute_I_II_Function(fptr: Pointer, p1: Int, p2: Int): Int
    @native def LLVMToolsExecute_B_II_Function(fptr: Pointer, p1: Int, p2: Int): Byte
    @native def LLVMToolsExecute_B_FF_Function(fptr: Pointer, p1: Float, p2: Float): Byte
  }

  // One-time initialization of LLVM
  tools.LLVMToolsInitializeAll()
}

trait NonNativeApi extends Library {
  def LLVMFunctionType(returnType: api.Type, paramTypes: Array[api.Type], numParams: Int, varArgs: Boolean): api.FunctionType
  def LLVMAddIncoming(phiNode: api.Value, incomingValues: Array[api.Value], incomingBlocks: Array[api.BasicBlock], count: Int): Unit
  def LLVMStructSetBody(struct: api.Type, elementTypes: Array[api.Type], elementCount: Int, packed: Boolean): Unit
  def LLVMGetStructElementTypes(structs: api.Type, destTypes: Array[api.Type]): Unit
  def LLVMGetParamTypes(functionType: api.Type, destTypes: Array[api.Type]): Unit
  def LLVMBuildCall(builder: api.Builder, fn: api.Value, args: Array[api.Value], argCnt: Int, name: String): api.Value

  def LLVMConstStructInContext(context: api.Context, constVals: Array[api.Value], count: Int, packed: Boolean): api.Value
  def LLVMConstNamedStruct(structTy: api.Type, constVals: Array[api.Value], count: Int): api.Value
  def LLVMBuildGEP(builder: api.Builder, ptr: api.Value, indices: Array[api.Value], numIndices: Int, name: String): api.Value
  def LLVMBuildGEP2(builder: api.Builder, typ: api.Type, ptr: api.Value, indices: Array[api.Value], numIndices: Int, name: String): api.Value
  def LLVMBuildInBoundsGEP2(builder: api.Builder, typ: api.Type, ptr: api.Value, indices: Array[api.Value], numIndices: Int, name: String): api.Value
}
