name := "scala-llvm"

version := "1.1"

scalaVersion := "2.13.6"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.13.6"
libraryDependencies += "net.java.dev.jna" % "jna" % "4.1.0"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.2.9" % "test"

// Compiles the CPP wrapper library
//compile <<= (Compile / compile, sourceDirectory, target, classDirectory in Compile).map { (compile, srcDir, targetDir, classDir) => {
//  val result = sbt.Process("cmake" :: s"${srcDir}/main/cpp" :: s"-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=$classDir/linux-x86-64" :: Nil, targetDir) #&&
//    sbt.Process("cmake" :: "--build" :: targetDir.toString :: Nil, targetDir) !
//
//  if (result != 0)
//    error("Error compiling wrapper library!")
//
//  compile
//}}