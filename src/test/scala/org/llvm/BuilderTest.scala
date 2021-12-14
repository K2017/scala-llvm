package org.llvm

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite


class BuilderTest extends AnyFunSuite with BeforeAndAfter {
  implicit var context: Context = null
  implicit var module: Module = null

  before {
    context = Context.create()
    module = Module.create("TestModule")
  }

  after {
    module.dispose()
    context.dispose()
  }

  test("A function that sums two values") {
    val i32 = context.Types.i32
    val function = Function.create("testFunction", i32, i32, i32)
    function := { implicit builder  =>
      val param0 = function.params(0) as "param0"
      val param1 = function.params(1) as "param1"
      val result = param0 + param1 as "result"
      builder.ret(result)
    }
    val functionStr = function.toString
    assert(functionStr.contains("%result = add i32 %param0, %param1"))
    assert(functionStr.contains("ret i32 %result"))
  }

  test("Insertion points can be saved") {
    val i32 = context.Types.i32
    val void = context.Types.void

    module.setSourceFile("TestFile")

    val strct = module.createStruct("struct", Nil)

    val f2 = Function.create("add1", i32, i32)
    f2.build {implicit builder =>
      val param = f2.params(0) as "param"
      val sum = param + 1
      builder.ret(sum)
    }
    val function = Function.create("testFunction", void, i32, i32)
    function.build { implicit builder  =>
      val param = function.params(0) as "param"

      val block1 = function.appendBasicBlock("block1") {
        val sum1 = param + 1 as "sum1"
        val sum2 = param + 2 as "sum2"
      }

      val cl = builder.call(f2, param) as "cl1"

      // sum3 should come before sum1 and sum2
      val sum3 = param + 3 as "sum3"
      builder.br(block1)
    }


    val functionStr = function.toString
    println(module.toString)
    assert(functionStr.contains("%sum1 = add i32 %param, 1"))
    assert(functionStr.contains("%sum3 = add i32 %param, 3"))
    assert(functionStr.indexOf("%sum3 = add i32 %param, 3") < functionStr.indexOf("%sum1 = add i32 %param, 1"))
  }

  test("Builder returns the correct insertion blocks and functions") {
    val function = Function.create("testFunction", context.Types.void)
    function.build { implicit builder =>
      val entryBlock = builder.currentBlock
      assert(builder.currentFunction === function)

      val block = function.appendBasicBlock("block")
      block := {
        assert(builder.currentBlock === block)
        assert(builder.currentFunction === function)
      }

      assert(builder.currentBlock === entryBlock)
    }
  }

}
