package com.igetcool.register.bean


public class RegisterInfo {

    /*
    * 注册的别名
    */
    def name

    /*
    * 扫描实现的接口
    */
    def scanInterface

    /*
    * 扫描实现接口的基类
    */
    String[] scanBaseInterface

    /*
    * 被注入的类
    */
    def injectedClass

    /*
    * 注入的方法
    */
    def injectMethod

    /*
    * 被注入的位置
    */
    def injectedPosition

    /*
    * 注入的方式
    * instance  如 new RouterA()
    * path      如 com.dedao.router.RouterA
    */
    def injectType

    File injectClassFile

    ArrayList<String> scanInterfaceFromProject = new ArrayList<>()

    public RegisterInfo(String name) {
        this.name = name
    }

    void transformClassInfoToPath() {
        scanInterface = transform(scanInterface)
        injectedClass = transform(injectedClass)
        if (scanBaseInterface) {
            int size = scanBaseInterface.size();
            for (int i = 0; i < size; i++) {
                scanBaseInterface[i] = transform(scanBaseInterface[i])
            }
        }
    }

    void setDefaultValue() {
        if (injectedPosition == null) {
            injectedPosition = "<clinit>"
        }
    }

    private String transform(String str) {
        return str ? str.replaceAll('\\.', File.separator).intern() : str
    }

    @Override
    String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        return stringBuffer.append("scanInterface:").append(scanInterface).append(", ")
                .append("injectedClass:").append(injectedClass).append(", ")
                .append("injectMethod:").append(injectMethod).append(", ")
                .append("injectType:").append(injectType).append(", ").toString()
    }
}