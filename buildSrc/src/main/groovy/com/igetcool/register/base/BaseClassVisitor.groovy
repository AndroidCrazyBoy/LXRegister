package com.igetcool.register.base

import org.objectweb.asm.ClassVisitor

class BaseClassVisitor extends ClassVisitor {

    BaseClassVisitor(int api, ClassVisitor cv) {
        super(api, cv)
    }

    public boolean isContainFlag(int access, int flag) {
        return (access & flag) == flag
    }
}