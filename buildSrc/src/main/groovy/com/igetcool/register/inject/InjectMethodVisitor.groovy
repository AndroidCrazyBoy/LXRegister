package com.igetcool.register.inject

import com.igetcool.register.bean.RegisterInfo
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class InjectMethodVisitor extends MethodVisitor {

    private RegisterInfo mInfo
    private boolean isStatic

    InjectMethodVisitor(int i, MethodVisitor methodVisitor, RegisterInfo info, boolean isStatic) {
        super(i, methodVisitor)
        mInfo = info
        this.isStatic = isStatic
    }

    @Override
    void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            mInfo.scanInterfaceFromProject.each { name ->

                if (isStatic) {
                    if (mInfo.injectType == 'string') {
                        mv.visitLdcInsn(name.replaceAll(File.separator, '.'))
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC
                                , mInfo.injectedClass
                                , mInfo.injectMethod
                                , "(Ljava/lang/String;)V"
                                , false)
                    } else {
                        mv.visitTypeInsn(Opcodes.NEW, name)
                        mv.visitInsn(Opcodes.DUP)
                        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, name, "<init>", "()V", false)
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC
                                , mInfo.injectedClass
                                , mInfo.injectMethod
                                , "(L${mInfo.scanInterface};)V"
                                , false)
                    }

                } else {
                    if (mInfo.injectType == 'string') {
                        mv.visitVarInsn(Opcodes.ALOAD, 0)
                        mv.visitLdcInsn(name.replaceAll(File.separator, '.'))
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL
                                , mInfo.injectedClass
                                , mInfo.injectMethod
                                , "(Ljava/lang/String;)V"
                                , false)
                    } else {
                        mv.visitVarInsn(Opcodes.ALOAD, 0)
                        mv.visitTypeInsn(Opcodes.NEW, name)
                        mv.visitInsn(Opcodes.DUP)
                        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, name, "<init>", "()V", false)
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL
                                , mInfo.injectedClass
                                , mInfo.injectMethod
                                , "(L${mInfo.scanInterface};)V"
                                , false)
                    }
                }
            }
        }
        super.visitInsn(opcode)
    }

    @Override
    void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 4, maxLocals)
    }
}