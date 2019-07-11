package com.igetcool.register.bean

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

public class RegisterConfig {

    static NamedDomainObjectContainer<RegisterInfo> container

    public RegisterConfig(Project project) {
        container = project.container(RegisterInfo)
    }

    void registerInfo(Action<NamedDomainObjectContainer<RegisterInfo>> action) {
        action.execute(container)
    }


    static NamedDomainObjectContainer<RegisterInfo> getRegisterConfig() {
        return container
    }

    @Override
    String toString() {
        "className = ${this.className}, ${registerInfo}"
    }

}