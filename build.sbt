name := "scala-llvm"

version := "1.2"

val llvmVersion = "13.0.1"

scalaVersion := "2.13.6"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.13.6"
libraryDependencies += "net.java.dev.jna" % "jna" % "5.9.0" % "provided"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
libraryDependencies += "org.bytedeco" % ("llvm" + "-platform") % (llvmVersion + "-1.5.7")

Compile / sourceGenerators += Def.task {

  val llvmJars = (Compile / managedClasspath).value filter {cp =>
    cp.data.name.startsWith(s"llvm-$llvmVersion-")
  }

  val javaCPP = ((Compile / managedClasspath).value filter {cp =>
    cp.data.name.startsWith(s"javacpp-")
  })

  val all = (Compile / managedClasspath).value ++ (Compile / unmanagedClasspath).value

  var files = List[File]()
  if (llvmJars.nonEmpty && javaCPP.nonEmpty) {
    val genName = "Flag"
    val genName2 = "MetadataKind"
    val file = (Compile / sourceManaged).value / "org.llvm.dwarf" / s"$genName.scala"
    val file2 = (Compile / sourceManaged).value / "org.llvm.dwarf" / s"$genName2.scala"
    IO.write(file,
      s"""package org.llvm.dwarf
         |
         |object $genName {
         |  sealed class $genName(val id: Int) {
         |    def |(other: $genName): $genName = new $genName(id | other.id)
         |  }
         |""".stripMargin)
    IO.write(file2,
      s"""package org.llvm.dwarf
         |
         |object $genName2 {
         |  sealed class $genName2(val id: Int)
         |
         |""".stripMargin)
    val classLoader = new java.net.URLClassLoader(llvmJars.map(_.data.toURI.toURL).toArray ++ javaCPP.map(_.data.toURI.toURL) ++ all.map(_.data.toURI.toURL))
    val llvmClass = Class.forName("org.bytedeco.llvm.global.LLVM", true, classLoader)
    val declared = llvmClass.getDeclaredFields
    declared filter {f =>
      f.getName.startsWith("LLVMDIFlag")
    } foreach { f=>
      val name = f.getName.replace("LLVMDIFlag", "")
      val flagVal = f.getInt(null)
      val caseClass = s"case object $name extends $genName($flagVal)"
      IO.write(file, s"""|  $caseClass\n""".stripMargin, append = true)
    }
    declared filter {
      _.getName.endsWith("MetadataKind")
    } foreach { f =>
      val name = f.getName.replace("MetadataKind", "").replace("LLVM", "")
      val flagVal = f.getInt(null)
      val caseClass = s"case object $name extends $genName2($flagVal)"
      IO.write(file2, s"""|  $caseClass\n""".stripMargin, append = true)
    }
    IO.write(file, """}""", append = true)
    IO.write(file2, """}""", append = true)
    files = files ++ List(file, file2)
  }

  files
}.taskValue

// Compiles the CPP wrapper library
//Compile / compile := {((Compile / compile).value, sourceDirectory.value, target.value, (Compile / classDirectory).value) match {
//  case (compile, srcDir, targetDir, classDir) =>
//    val result =
//      Process("cmake" :: s"${srcDir.toString}/main/cpp" :: s"-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=${classDir.toString}/linux-x86-64" :: Nil, targetDir) #&&
//      Process("cmake" :: "--build" :: targetDir.toString :: Nil, targetDir) !
//
//    if (result != 0)
//      sys.error("Error compiling wrapper library!")
//
//    compile
//}}

assemblyJarName := "scala-llvm.jar"
assemblyCacheOutput := false
assemblyOutputPath := target.value
assemblyPackageScala / assembleArtifact := false
