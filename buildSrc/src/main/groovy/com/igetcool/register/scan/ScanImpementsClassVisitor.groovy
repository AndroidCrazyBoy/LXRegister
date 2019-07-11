package com.igetcool.register.scan

import com.igetcool.register.base.BaseClassVisitor
import com.igetcool.register.bean.RegisterConfig
import com.igetcool.register.bean.RegisterInfo
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes


class ScanImpementsClassVisitor extends BaseClassVisitor {

    static final String EXTEND_OBJECT = 'java/lang/Object'

    public ScanImpementsClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)

        if (isContainFlag(access, Opcodes.ACC_ABSTRACT)
                || isContainFlag(access, Opcodes.ACC_INTERFACE)
                || !isContainFlag(access, Opcodes.ACC_PUBLIC)) {

            return
        }

        RegisterConfig.container.each { RegisterInfo info ->

            if (info.scanBaseInterface != null && info.scanBaseInterface.size() != 0) {
                def filter = info.scanBaseInterface.find { it == name }
                if (filter) return
            }

            if (superName != EXTEND_OBJECT && info.scanBaseInterface != null &&
                    info.scanBaseInterface.size() != 0) {
                def interfaceName = info.scanBaseInterface.find { it == superName }
                if (interfaceName) {
                    info.scanInterfaceFromProject.add(name)
                    return
                }
            }

            if (info.scanInterface && interfaces != null) {
                def interfaceName = interfaces.find { info.scanInterface == it }
                if (interfaceName) {
                    info.scanInterfaceFromProject.add(name)
                }
            }
        }
    }
}