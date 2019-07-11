package com.igetcool.register.inject

import com.igetcool.register.base.BaseClassVisitor
import com.igetcool.register.bean.RegisterInfo
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class InjectCodeClassVisitor extends BaseClassVisitor {

    private RegisterInfo mInfo

    public InjectCodeClassVisitor(int api, ClassVisitor cv, RegisterInfo info) {
        super(api, cv)
        this.mInfo = info
    }


    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        def mv = super.visitMethod(access, name, desc, signature, exceptions)
        println('mInfo.injectedPosition ' + mInfo.injectedPosition + " name = " + name + "  desc " + desc)
        if (mInfo.injectedPosition == name) {

            println('找到啦 哈哈哈哈哈哈 ' + name + " signature = " + signature )
            boolean isStatic = isContainFlag(access, Opcodes.ACC_STATIC)
            mv = new InjectMethodVisitor(Opcodes.ASM5, mv, mInfo, isStatic)
        }

        return mv
    }
}