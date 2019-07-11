package com.igetcool.register.util

import com.igetcool.register.bean.RegisterConfig
import org.gradle.api.Project

public class RegisterPrinter {

    static void printRegisterConfigs(Project project) {
        project.afterEvaluate {

            RegisterConfig.container.each { info ->
                println()
                println("+---name              = " + info.name)
                println("+---scanInterface     = " + info.scanInterface)
                println("+---scanBaseInterface = " + (info.scanBaseInterface ? info.scanBaseInterface.toString() : null))
                println("+---injectedClass     = " + info.injectedClass)
                println("+---injectedPosition  = " + info.injectedPosition)
                println("+---injectMethod      = " + info.injectMethod)
                println("+---injectType        = " + info.injectType)
            }
        }
    }
}