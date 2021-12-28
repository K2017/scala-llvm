package org

import scala.language.implicitConversions

package object llvm {
  class LLVMException(what: String) extends Exception(what)

  implicit def booleanToCInt(boolean: Boolean): Int = if (boolean) 1 else 0
}
