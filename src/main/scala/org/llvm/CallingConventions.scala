package org.llvm

object CallingConventions extends Enumeration {
  type CallingConvention = Value
  /// C - The default llvm calling convention, compatible with C.  This
  /// convention is the only calling convention that supports varargs calls.
  /// As with typical C calling conventions, the callee/caller have to
  /// tolerate certain amounts of prototype mismatch.
  val C: CallingConvention = Value(0)

  // Generic LLVM calling conventions.  None of these calling conventions
  // support varargs calls, and all assume that the caller and callee
  // prototype exactly match.

  /// Fast - This calling convention attempts to make calls as fast as
  /// possible (e.g. by passing things in registers).
  val Fast: CallingConvention =  Value(8)

  // Cold - This calling convention attempts to make code in the caller as
  // efficient as possible under the assumption that the call is not commonly
  // executed.  As such  these calls often preserve all registers so that the
  // call does not break any live ranges in the caller side.
  val Cold: CallingConvention =  Value(9)

  // GHC - Calling convention used by the Glasgow Haskell Compiler (GHC).
  val GHC: CallingConvention = Value(10)

  // HiPE - Calling convention used by the High-Performance Erlang Compiler
  // (HiPE).
  val HiPE: CallingConvention = Value(11)

  // WebKit JS - Calling convention for stack based JavaScript calls
  val WebKit_JS: CallingConvention = Value(12)

  // AnyReg - Calling convention for dynamic register based calls (e.g.
  // stackmap and patchpoint intrinsics).
  val AnyReg: CallingConvention = Value(13)

  // PreserveMost - Calling convention for runtime calls that preserves most
  // registers.
  val PreserveMost: CallingConvention = Value(14)

  // PreserveAll - Calling convention for runtime calls that preserves
  // (almost) all registers.
  val PreserveAll: CallingConvention = Value(15)

  // Swift - Calling convention for Swift.
  val Swift: CallingConvention = Value(16)

  // CXX_FAST_TLS - Calling convention for access functions.
  val CXX_FAST_TLS: CallingConvention = Value(17)

  /// Tail - This calling convention attemps to make calls as fast as
  /// possible while guaranteeing that tail call optimization can always
  /// be performed.
  val Tail: CallingConvention = Value(18)

  /// Special calling convention on Windows for calling the Control
  /// Guard Check ICall funtion. The function takes exactly one argument
  /// (address of the target function) passed in the first argument register
  /// and has no return value. All register values are preserved.
  val CFGuard_Check: CallingConvention = Value(19)

  /// SwiftTail - This follows the Swift calling convention in how arguments
  /// are passed but guarantees tail calls will be made by making the callee
  /// clean up their stack.
  val SwiftTail: CallingConvention = Value(20)

  // Target - This is the start of the target-specific calling conventions
  // e.g. fastcall and thiscall on X86.
  val FirstTargetCC: CallingConvention = Value(63)

  /// X86_StdCall - stdcall is the calling conventions mostly used by the
  /// Win32 API. It is basically the same as the C convention with the
  /// difference in that the callee is responsible for popping the arguments
  /// from the stack.
  val X86_StdCall: CallingConvention = Value(64)

  /// X86_FastCall - 'fast' analog of X86_StdCall. Passes first two arguments
  /// in ECX:EDX registers  others - via stack. Callee is responsible for
  /// stack cleaning.
  val X86_FastCall: CallingConvention = Value(65)

  /// ARM_APCS - ARM Procedure Calling Standard calling convention (obsolete
  /// but still used on some targets).
  val ARM_APCS: CallingConvention = Value(66)

  /// ARM_AAPCS - ARM Architecture Procedure Calling Standard calling
  /// convention (aka EABI). Soft float variant.
  val ARM_AAPCS: CallingConvention = Value(67)

  /// ARM_AAPCS_VFP - Same as ARM_AAPCS  but uses hard floating point ABI.
  val ARM_AAPCS_VFP: CallingConvention = Value(68)

  /// MSP430_INTR - Calling convention used for MSP430 interrupt routines.
  val MSP430_INTR: CallingConvention = Value(69)

  /// X86_ThisCall - Similar to X86_StdCall. Passes first argument in ECX
  /// others via stack. Callee is responsible for stack cleaning. MSVC uses
  /// this by default for methods in its ABI.
  val X86_ThisCall: CallingConvention = Value(70)

  /// PTX_Kernel - Call to a PTX kernel.
  /// Passes all arguments in parameter space.
  val PTX_Kernel: CallingConvention = Value(71)

  /// PTX_Device - Call to a PTX device function.
  /// Passes all arguments in register or parameter space.
  val PTX_Device: CallingConvention = Value(72)

  /// SPIR_FUNC - Calling convention for SPIR non-kernel device functions.
  /// No lowering or expansion of arguments.
  /// Structures are passed as a pointer to a struct with the byval attribute.
  /// Functions can only call SPIR_FUNC and SPIR_KERNEL functions.
  /// Functions can only have zero or one return values.
  /// Variable arguments are not allowed  except for printf.
  /// How arguments/return values are lowered are not specified.
  /// Functions are only visible to the devices.
  val SPIR_FUNC: CallingConvention = Value(75)

  /// SPIR_KERNEL - Calling convention for SPIR kernel functions.
  /// Inherits the restrictions of SPIR_FUNC  except
  /// Cannot have non-void return values.
  /// Cannot have variable arguments.
  /// Can also be called by the host.
  /// Is externally visible.
  val SPIR_KERNEL: CallingConvention = Value(76)

  /// Intel_OCL_BI - Calling conventions for Intel OpenCL built-ins
  val Intel_OCL_BI: CallingConvention = Value(77)

  /// The C convention as specified in the x86-64 supplement to the
  /// System V ABI  used on most non-Windows systems.
  val X86_64_SysV: CallingConvention = Value(78)

  /// The C convention as implemented on Windows/x86-64 and
  /// AArch64. This convention differs from the more common
  /// \c X86_64_SysV convention in a number of ways  most notably in
  /// that XMM registers used to pass arguments are shadowed by GPRs
  /// and vice versa.
  /// On AArch64  this is identical to the normal C (AAPCS) calling
  /// convention for normal functions  but floats are passed in integer
  /// registers to variadic functions.
  val Win64: CallingConvention = Value(79)

  /// MSVC calling convention that passes vectors and vector aggregates
  /// in SSE registers.
  val X86_VectorCall: CallingConvention = Value(80)

  /// Calling convention used by HipHop Virtual Machine (HHVM) to
  /// perform calls to and from translation cache  and for calling PHP
  /// functions.
  /// HHVM calling convention supports tail/sibling call elimination.
  val HHVM: CallingConvention = Value(81)

  /// HHVM calling convention for invoking C/C++ helpers.
  val HHVM_C: CallingConvention = Value(82)

  /// X86_INTR - x86 hardware interrupt context. Callee may take one or two
  /// parameters  where the 1st represents a pointer to hardware context frame
  /// and the 2nd represents hardware error code  the presence of the later
  /// depends on the interrupt vector taken. Valid for both 32- and 64-bit
  /// subtargets.
  val X86_INTR: CallingConvention = Value(83)

  /// Used for AVR interrupt routines.
  val AVR_INTR: CallingConvention = Value(84)

  /// Calling convention used for AVR signal routines.
  val AVR_SIGNAL: CallingConvention = Value(85)

  /// Calling convention used for special AVR rtlib functions
  /// which have an "optimized" convention to preserve registers.
  val AVR_BUILTIN: CallingConvention = Value(86)

  /// Calling convention used for Mesa vertex shaders  or AMDPAL last shader
  /// stage before rasterization (vertex shader if tessellation and geometry
  /// are not in use  or otherwise copy shader if one is needed).
  val AMDGPU_VS: CallingConvention = Value(87)

  /// Calling convention used for Mesa/AMDPAL geometry shaders.
  val AMDGPU_GS: CallingConvention = Value(88)

  /// Calling convention used for Mesa/AMDPAL pixel shaders.
  val AMDGPU_PS: CallingConvention = Value(89)

  /// Calling convention used for Mesa/AMDPAL compute shaders.
  val AMDGPU_CS: CallingConvention = Value(90)

  /// Calling convention for AMDGPU code object kernels.
  val AMDGPU_KERNEL: CallingConvention = Value(91)

  /// Register calling convention used for parameters transfer optimization
  val X86_RegCall: CallingConvention = Value(92)

  /// Calling convention used for Mesa/AMDPAL hull shaders (= tessellation
  /// control shaders).
  val AMDGPU_HS: CallingConvention = Value(93)

  /// Calling convention used for special MSP430 rtlib functions
  /// which have an "optimized" convention using additional registers.
  val MSP430_BUILTIN: CallingConvention = Value(94)

  /// Calling convention used for AMDPAL vertex shader if tessellation is in
  /// use.
  val AMDGPU_LS: CallingConvention = Value(95)

  /// Calling convention used for AMDPAL shader stage before geometry shader
  /// if geometry is in use. So either the domain (= tessellation evaluation)
  /// shader if tessellation is in use  or otherwise the vertex shader.
  val AMDGPU_ES: CallingConvention = Value(96)

  // Calling convention between AArch64 Advanced SIMD functions
  val AArch64_VectorCall: CallingConvention = Value(97)

  /// Calling convention between AArch64 SVE functions
  val AArch64_SVE_VectorCall: CallingConvention = Value(98)

  /// Calling convention for emscripten __invoke_* functions. The first
  /// argument is required to be the function ptr being indirectly called.
  /// The remainder matches the regular calling convention.
  val WASM_EmscriptenInvoke: CallingConvention = Value(99)

  /// Calling convention used for AMD graphics targets.
  val AMDGPU_Gfx: CallingConvention = Value(100)

  /// M68k_INTR - Calling convention used for M68k interrupt routines.
  val M68k_INTR: CallingConvention = Value(101)

  /// The highest possible calling convention ID. Must be some 2^k - 1.
  val MaxID: CallingConvention = Value(1023)
}
