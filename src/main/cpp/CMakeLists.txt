cmake_minimum_required(VERSION 3.13.4)
project(SnelLowLevelAPI)

find_package(LLVM REQUIRED CONFIG)
include_directories(${LLVM_INCLUDE_DIRS})
link_directories(${LLVM_LIBRARY_DIRS})
add_definitions(${LLVM_DEFINITIONS})
llvm_map_components_to_libnames(LLVM_LIBS mcjit native)

# Pthreads
find_package(Threads)

# Crank up error reporting
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

add_library(LLVMTools SHARED
    llvm_tools.cpp
)
# Note: we do NOT link LLVM against the shared lib because LLVM will already be running in the
# host application's address space.
