package org.llvm

class BasicBlock(val name: String, val function: Function) {
  val llvmBasicBlock = api.LLVMAppendBasicBlockInContext(function.module.context, function, name)

  def build(bodyBuilder: Builder => Unit)(implicit builder: Builder): this.type = {
    builder.pushIP()
    builder.insertAtEndOfBlock(this)
    bodyBuilder(builder)
    builder.popIP()
    this
  }
  def apply(bodyBuilder: Builder=> Unit)(implicit builder: Builder) = build(bodyBuilder)(builder)
}

object BasicBlock {
  implicit def basicBlockToLLVM(bb: BasicBlock): api.BasicBlock = bb.llvmBasicBlock
}

class FunctionType(val returnType: Type, val argsTypes: Type*)(implicit val module: Module) extends Type {
  private val llvmArgsTypes: Seq[api.Type] = argsTypes map { _.llvmType }

  val llvmFunctionType = api.LLVMFunctionType(returnType, llvmArgsTypes.toArray, llvmArgsTypes.length, 0)
  val llvmType = llvmFunctionType
}

class Function(functionName: String, val returnType: Type, val argsTypes: Type*)(implicit val module: Module) extends Value {
  val functionType = new FunctionType(returnType, argsTypes: _*)
  val llvmFunction = api.LLVMAddFunction(module, functionName, functionType)
  val llvmValue = llvmFunction
  lazy val args: Array[Value] = (0 until argsTypes.length).map(i => new SSAValue(api.LLVMGetParam(this, i))).toArray

  def appendBasicBlock(name: String): BasicBlock = new BasicBlock(name, this)

  def build(bodyBuilder: Builder => Unit): this.type = {
    val builder = new Builder
    this.appendBasicBlock("init").build(bodyBuilder)(builder)
    builder.dispose()
    this
  }
  def apply(bodyBuilder: Builder => Unit) = build(bodyBuilder)
}

object Function {
  implicit def functionToLLVM(function: Function): api.Value = function.llvmFunction
}
