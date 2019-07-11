package com.igetcool.register.inject

import com.igetcool.register.bean.RegisterConfig
import com.igetcool.register.bean.RegisterInfo
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class InjectClassTask {

    static void injectTargetClass() {
        RegisterConfig.container.each { RegisterInfo info ->

            if (info.injectClassFile) {
                println('')
                println("#Register, Start injecting code :" + info.injectClassFile.absolutePath)
                if (info.scanInterfaceFromProject.isEmpty()) {
                    println("#Register, No scan to implement ${info.scanInterface} interface!!!")
                } else {
                    info.scanInterfaceFromProject.each {
                        println(it)
                    }

                    inject(info)
                }
            } else {
                println("#Register, Not found injectedClass : ${info.injectedClass} !!!")
            }
        }
    }

    static void inject(RegisterInfo info) {
        if (info.injectClassFile.absolutePath.contains('.jar')) {
            injectJarFile(info)
        } else {
            injectDirectoryFile(info)
        }
    }

    private static injectJarFile(RegisterInfo info) {
        File injectJarFile = info.injectClassFile

        if (!injectJarFile) {
            println("#Register ${info.name} injectClassFile is null.")
            return
        }

        def optJar = new File(injectJarFile.getParent(), injectJarFile.name + ".temp")
        if (optJar.exists())
            optJar.delete()

        def file = new JarFile(injectJarFile)
        Enumeration enumeration = file.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = file.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)
            def temp = entryName.replaceAll('.class', '')
            if (temp == info.injectedClass) {
                def bytes = injectByClassVisitor(inputStream, info)
                jarOutputStream.write(bytes)
            } else {
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
            }
            inputStream.close()
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        file.close()

        if (injectJarFile.exists()) {
            injectJarFile.delete()
        }
        optJar.renameTo(injectJarFile)

    }

    private static injectDirectoryFile(RegisterInfo info) {

        File injectFile = info.injectClassFile

        if (!injectFile) {
            println("#Register ${info.name} injectClassFile is null.")
            return
        }

        def tempClass = new File(injectFile.getParent(), injectFile.name + ".temp")

        FileInputStream inputStream = new FileInputStream(injectFile)
        FileOutputStream outputStream = new FileOutputStream(tempClass)

        def bytes = injectByClassVisitor(inputStream, info)

        outputStream.write(bytes)
        inputStream.close()
        outputStream.close()
        if (injectFile.exists()) {
            injectFile.delete()
        }

        tempClass.renameTo(injectFile)
    }


    private static byte[] injectByClassVisitor(InputStream inputStream, RegisterInfo info) {

        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        InjectCodeClassVisitor cv = new InjectCodeClassVisitor(Opcodes.ASM5, cw, info)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }
}