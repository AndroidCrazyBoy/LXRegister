package com.igetcool.register.scan

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.igetcool.register.bean.RegisterConfig
import com.igetcool.register.bean.RegisterInfo
import com.igetcool.register.util.InjectFilter
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

class ScanClassTask {

    static void scanDirectoryInputs(DirectoryInput directoryInput, File dest) {

        def path = directoryInput.file.absolutePath

        directoryInput.file.eachFileRecurse { File file ->
            def filePath = file.absolutePath
            def fileRelativelyPath = filePath.replace(path + File.separator, '')
            scanDirectory(file, fileRelativelyPath, dest.absolutePath)
        }
    }

    static void scanJarInputs(JarInput jarInput, File dest) {

        def file = jarInput.file
        def srcFilePath = file.absolutePath
        def jarFile = new JarFile(file)
        Enumeration enumeration = jarFile.entries()

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String fileRelativelyPath = jarEntry.getName()

            if (InjectFilter.filter(fileRelativelyPath)) {
                continue
            }

            scanJar(jarFile, jarEntry, fileRelativelyPath, dest);
        }
        if (null != file) {
            jarFile.close()
        }
    }

    private static void scanDirectory(File file, String fileRelativelyPath, String fileAbsolutePath) {
        if (InjectFilter.filter(fileRelativelyPath)) return
        def injectedClass = fileRelativelyPath.replaceAll('.class', '')
        scanInjectClass(injectedClass, fileRelativelyPath, fileAbsolutePath)
        scanImplementsClass(file.newDataInputStream())
    }

    private static void scanJar(JarFile jarFile, JarEntry jarEntry, String fileRelativelyPath, File dest) {
        if (InjectFilter.filter(fileRelativelyPath)) return
        def injectedClass = fileRelativelyPath.replaceAll('.class', '')
        scanJarInjectClass(injectedClass, fileRelativelyPath, dest)
        scanImplementsClass(jarFile.getInputStream(jarEntry))
    }

    /*
    *  检索注入的类
    */

    private static void scanJarInjectClass(String fileRelativelyName, String fileRelativelyPath, File file) {
        def infos = RegisterConfig.getRegisterConfig()
        infos.each { RegisterInfo info ->
            if (fileRelativelyName == info.injectedClass) {
                info.injectClassFile = file
            }
        }
    }

    /*
    *  检索注入的类
    */

    private static void scanInjectClass(String fileRelativelyName, String fileRelativelyPath, String fileAbsolutePath) {
        def infos = RegisterConfig.getRegisterConfig()
        infos.each { RegisterInfo info ->

            if (fileRelativelyName == info.injectedClass) {
                def filePath = fileAbsolutePath + File.separator + fileRelativelyPath
                info.injectClassFile = new File(filePath)
            }
        }
    }

    /*
    *  检索实现固定接口的类
    */

    private static void scanImplementsClass(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ScanImpementsClassVisitor cv = new ScanImpementsClassVisitor(Opcodes.ASM5, cw/*, filePath*/)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        inputStream.close()
    }
}