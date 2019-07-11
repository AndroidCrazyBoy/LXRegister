package com.igetcool.register

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.igetcool.register.inject.InjectClassTask
import com.igetcool.register.scan.ScanClassTask
import org.apache.commons.io.FileUtils

class RegisterTransform extends Transform {

    @Override
    String getName() {
        return "com.asm.register"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /*
    * true 支持增量
    */

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        transformInvocation.inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                //处理Jar
                processJarInputs(jarInput, outputProvider)
            }

            input.directoryInputs.each { DirectoryInput directoryInput ->
                //处理源码文件
                processDirectoryInputs(directoryInput, outputProvider)
            }
        }

        InjectClassTask.injectTargetClass()
    }

    void processJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {
        File dest = outputProvider.getContentLocation(
                jarInput.getFile().getAbsolutePath(),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR)

        ScanClassTask.scanJarInputs(jarInput, dest)

        FileUtils.copyFile(jarInput.getFile(), dest)
    }

    void processDirectoryInputs(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        File dest = outputProvider.getContentLocation(
                directoryInput.getName(),
                directoryInput.getContentTypes(),
                directoryInput.getScopes(),
                Format.DIRECTORY)


        println('processDirectoryInputs ' + directoryInput.file.absolutePath + "   " + dest.absolutePath);

        ScanClassTask.scanDirectoryInputs(directoryInput, dest)

        FileUtils.forceMkdir(dest)
        FileUtils.copyDirectory(directoryInput.getFile(), dest)
    }
}