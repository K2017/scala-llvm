import scala.sys.process.Process

name := "scala-llvm"

version := "1.1"

scalaVersion := "2.13.6"


libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.13.6"
libraryDependencies += "net.java.dev.jna" % "jna" % "5.9.0" % "provided"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"

// Compiles the CPP wrapper library
Compile / compile := {((Compile / compile).value, sourceDirectory.value, target.value, (Compile / classDirectory).value) match {
  case (compile, srcDir, targetDir, classDir) =>
    println(targetDir)

    val result =
      Process("cmake" :: s"${srcDir.toString}/main/cpp" :: s"-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=${classDir.toString}/linux-x86-64" :: Nil, targetDir) #&&
      Process("cmake" :: "--build" :: targetDir.toString :: Nil, targetDir) !

    if (result != 0)
      sys.error("Error compiling wrapper library!")

    compile
}}

assemblyJarName := "scala-llvm.jar"
assemblyCacheOutput := false
assemblyOutputPath := target.value
assemblyPackageScala / assembleArtifact := false