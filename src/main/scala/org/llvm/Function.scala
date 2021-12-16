package org.llvm

import org.llvm.CallingConventions.CallingConvention

class FunctionType(llvmType: api.Type) extends Type(llvmType) {
  lazy val returnType: Type = Type.resolveLLVMType(api.LLVMGetReturnType(this))
  lazy val paramsTypes: Array[Type] = {
    val numParams = api.LLVMCountParamTypes(this)
    val types = new Array[api.Type](numParams)
    api.LLVMGetParamTypes(this, types)
    types.map(Type.resolveLLVMType(_))
  }
}

object FunctionType {
  def create(returnType: Type, paramsTypes: Type*)(implicit module: Module, variadic: Boolean = false): FunctionType = {
    val llvmArgsTypes: Array[api.Type] = paramsTypes.map(_.llvmType).toArray
    val llvmFunctionType = api.LLVMFunctionType(returnType, llvmArgsTypes, llvmArgsTypes.length, variadic)
    new FunctionType(llvmFunctionType)
  }
}

class Function(val llvmValue: api.Value)(implicit val module: Module) extends Value {
  // For some reason, LLVM returns the function type as a pointer to the function
  // type instead of the function type directly
  lazy val functionType: FunctionType = getType.asInstanceOf[PointerType].pointedType.asInstanceOf[FunctionType]
  lazy val returnType: Type = functionType.returnType
  lazy val paramsTypes: Array[Type] = functionType.paramsTypes
  lazy val params: Array[Value] = paramsTypes.indices.map(i => new SSAValue(api.LLVMGetParam(this, i))).toArray

  def appendBasicBlock(name: String): BasicBlock = BasicBlock(api.LLVMAppendBasicBlockInContext(module.context, this, name))

  def setCallingConvention(conv: CallingConvention): Unit = api.LLVMSetFunctionCallConv(this, conv.id)

  def build(bodyBuilder: Builder => Unit): this.type = {
    val builder = new Builder
    val initBlock = this.appendBasicBlock("init")
    builder.insertAtEndOfBlock(initBlock)
    bodyBuilder(builder)
    builder.dispose()
    this
  }
  def := : (Builder => Unit) => Function.this.type = build

  def apply(args: Value*)(implicit builder: Builder): Instruction = {
    builder.call(this, args: _*)
  }
}

object Function {
  def create(name: String, returnType: Type, paramsTypes: Type*)(implicit module: Module, variadic: Boolean = false): Function = {
    val functionType = FunctionType.create(returnType, paramsTypes: _*)(variadic = variadic, module = module)
    val llvmFunction = api.LLVMAddFunction(module, name, functionType)
    new Function(llvmFunction)
  }
}
