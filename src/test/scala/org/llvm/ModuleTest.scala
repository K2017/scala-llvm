package org.llvm

import org.scalatest.{BeforeAndAfter, FunSuite}

class ModuleTest extends FunSuite with BeforeAndAfter {
  implicit var context: Context = null
  implicit var module: Module = null

  before {
    context = new Context
    module = null
  }

  after {
    if (module != null)
      module.dispose()
    context.dispose()
  }

  test("A module can be created") {
    module = new Module("TestModule")
    assert(module.toString.contains("; ModuleID = 'TestModule'"))
  }

  test("A function can be created") {
    module = new Module("TestModule")
    val function = new Function("testFunction", module.voidType, module.int32Type)
    assert(function.name === "testFunction")
    assert(function.toString.contains("declare void @testFunction(i32)"))
  }

  test("We can create global variables") {
    module = new Module("TestModule")

    module.addGlobalVariable(module.int32Type, "globalVar")
    assert(module.toString.contains("@globalVar"))
  }

  test("We can create, compare and resolve structures") {
    module = new Module("TestModule")

    val testStruct1 = module.createStruct("testStruct1", Seq(module.int32Type, module.floatType))
    assert(testStruct1.name === "testStruct1")

    // If we don't instantiate the struct, it will not be created in the module
    val globalVar1 = module.addGlobalVariable(testStruct1, "globalVar1")
    assert(module.toString.contains("%testStruct1 = type { i32, float }"))

    val testStruct2 = module.createStruct("testStruct2", Seq(module.int32Type, module.floatType))
    // Even though they are equivalent, these do NOT map to the same type
    assert(testStruct1 !== testStruct2)

    // Test packed structs
    val packedStruct1 = module.createStruct("packedStruct1", Seq(module.int32Type, module.floatType), true)
    val globalVar2 = module.addGlobalVariable(packedStruct1, "globalVar2")
    assert(module.toString.contains("%packedStruct1 = type <{ i32, float }>"))

    val resolvedType = globalVar1.getType
    assert(resolvedType.isInstanceOf[PointerType])
    assert(resolvedType.asInstanceOf[PointerType].pointedType === testStruct1)

    assert(testStruct1.elements(0).isInstanceOf[Int32Type])
    assert(testStruct1.elements(1).isInstanceOf[FloatType])
    //println(module)
  }
}
