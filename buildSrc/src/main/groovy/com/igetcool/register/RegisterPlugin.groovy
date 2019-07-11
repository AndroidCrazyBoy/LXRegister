package com.igetcool.register

import com.android.build.gradle.AppExtension
import com.igetcool.register.bean.RegisterConfig
import com.igetcool.register.util.InjectFilter
import com.igetcool.register.util.RegisterPrinter
import org.gradle.api.Plugin
import org.gradle.api.Project

class RegisterPlugin implements Plugin<Project> {

    public static final def REGISTER_CONFIG = 'register'

    @Override
    void apply(Project project) {
        println('Welcome to RegisterPlugin!')

        initExtensions(project)

        initTransform(project)
    }

    void initTransform(Project project) {
        RegisterTransform transform = new RegisterTransform()
        project.getExtensions().findByType(AppExtension).registerTransform(transform)
    }

    void initExtensions(Project project) {
        RegisterConfig.container = null
        project.getExtensions().create(REGISTER_CONFIG, RegisterConfig, project)

        InjectFilter.filterRegisterInfo(project)

        RegisterPrinter.printRegisterConfigs(project)
    }
}