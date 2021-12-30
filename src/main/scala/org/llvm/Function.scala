package org.llvm

import org.bytedeco.javacpp.PointerPointer
import org.bytedeco.llvm.LLVM.{LLVMTypeRef, LLVMValueRef}
import org.bytedeco.llvm.global.LLVM.{LLVMAddFunction, LLVMAppendBasicBlockInContext, LLVMCountParamTypes, LLVMDeleteFunction, LLVMFunctionType, LLVMGetFunctionCallConv, LLVMGetParam, LLVMGetParamTypes, LLVMGetReturnType, LLVMGetSubprogram, LLVMSetFunctionCallConv, LLVMSetSubprogram}
import org.llvm.CallingConvention.CallingConvention
import org.llvm.dwarf.DISubprogram

class FunctionType(llvmType: LLVMTypeRef) extends Type(llvmType) {
  lazy val returnType: Type = Type.resolveLLVMType(LLVMGetReturnType(this))
  lazy val paramsTypes: Array[Type] = {
    val numParams = LLVMCountParamTypes(this)
    val types = new PointerPointer[LLVMTypeRef](numParams)
    LLVMGetParamTypes(this, types)
    (for (i <- 0 until numParams) yield Type.resolveLLVMType(types.get(classOf[LLVMTypeRef], i))).toArray
  }
}

object FunctionType {
  def create(returnType: Type, paramsTypes: Type*)(implicit module: Module, variadic: Boolean = false): FunctionType = {
    val llvmArgsTypes = new PointerPointer[LLVMTypeRef](paramsTypes.map(_.llvmType): _*)
    val llvmFunctionType = LLVMFunctionType(returnType, llvmArgsTypes, paramsTypes.length, variadic)
    new FunctionType(llvmFunctionType)
  }
}

class Function(val llvmValue: LLVMValueRef)(implicit val module: Module) extends Value {
  // For some reason, LLVM returns the function type as a pointer to the function
  // type instead of the function type directly
  lazy val functionType: FunctionType = getType.asInstanceOf[PointerType].pointedType.asInstanceOf[FunctionType]
  lazy val returnType: Type = functionType.returnType
  lazy val paramsTypes: Array[Type] = functionType.paramsTypes
  lazy val params: Array[Value] = paramsTypes.indices.map(i => new SSAValue(LLVMGetParam(this, i))).toArray

  def appendBasicBlock(name: String): BasicBlock = BasicBlock(LLVMAppendBasicBlockInContext(module.context, this, name))

  def setCallingConvention(conv: CallingConvention): Unit = LLVMSetFunctionCallConv(this, conv.id)
  def getCallingConvention: CallingConvention = CallingConvention(LLVMGetFunctionCallConv(this))

  def setSubprogram(subprogram: DISubprogram): Unit = LLVMSetSubprogram(this, subprogram)
//  def getSubprogram: DISubprogram = new DISubprogram(LLVMGetSubprogram(this))

  def build(bodyBuilder: Builder => Unit): this.type = {
    val builder = new Builder
    val initBlock = this.appendBasicBlock("entry")
    builder.insertAtEndOfBlock(initBlock)
    bodyBuilder(builder)
    builder.dispose()
    this
  }
  def := : (Builder => Unit) => Function.this.type = build

  def apply(args: Value*)(implicit builder: Builder): Instruction = {
    builder.call(this, args: _*)
  }

  def erase(): Unit = LLVMDeleteFunction(this)
}

object Function {
  private def _create(name: String, ret: Type, params: Type*)(implicit module: Module, variadic: Boolean): Function ={
    val functionType = FunctionType.create(ret, params: _*)(module, variadic)
    val llvmFunction = LLVMAddFunction(module, name, functionType)
    new Function(llvmFunction)
  }
  def create(name: String, returnType: Type, paramsTypes: Type*)(implicit module: Module): Function = {
    _create(name, returnType, paramsTypes: _*)(module, variadic = false)
  }
  def createVariadic(name: String, returnType: Type, paramsTypes: Type*)(implicit module: Module): Function = {
    _create(name, returnType, paramsTypes: _*)(module, variadic = true)
  }
}
