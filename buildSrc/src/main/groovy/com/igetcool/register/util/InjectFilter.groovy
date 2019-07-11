package com.igetcool.register.util

import com.igetcool.register.bean.RegisterConfig
import com.igetcool.register.bean.RegisterInfo
import org.gradle.api.Project

class InjectFilter {

    private static ArrayList<String> filterKeyList = new ArrayList<>()

    static {
        filterKeyList.add('R$')
        filterKeyList.add('R.class')
        filterKeyList.add('META-INF')
        filterKeyList.add('androidx')
        filterKeyList.add('android/arch')
        filterKeyList.add('android/support')
        filterKeyList.add('BuildConfig.class')
    }

    static boolean filter(String str) {
        if (str == null || str == '') return true
        for (key in filterKeyList) {
            if (str.contains(key) || !str.endsWith('.class')) {
                return true
            }
        }
        return false
    }

    static void filterRegisterInfo(Project project) {
        project.afterEvaluate {
            Iterator<RegisterInfo> iterator = RegisterConfig.container.iterator()
            while (iterator.hasNext()) {
                RegisterInfo info = iterator.next();
                if (isStrEmpty(info.injectedClass) ||
                        isStrEmpty(info.scanInterface) ||
                        isStrEmpty(info.injectMethod)) {
                    iterator.remove()
                    println('RegisterConfig = '+info.name+' has invalid value, please check!')
                }
            }

            RegisterConfig.container.each { info ->
                info.transformClassInfoToPath()
                info.setDefaultValue()
            }
        }
    }

    private static boolean isStrEmpty(String str) {
        return str == null || str == ""
    }
}