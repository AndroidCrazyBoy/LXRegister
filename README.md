# IGCRegister

## 自动注册
```
为了提供各个模块内容解耦，如路由、登录回调、js扩展方法等等提供自动注册方式。
```
#### 使用方式
- 添加插件 apply plugin: 'com.igetcool.register'
- 添加配置

```
register {
    registerInfo {
        router {
            scanInterface = 'com.luojilab.dedao.component.activator.IActivator'
            injectedClass = 'com.dedao.juvenile.base.creator.RouterRegisterCreator'
            injectMethod = 'registerRouter'
            injectedPosition = 'registerAllRouterMaps'
            injectType = 'string'
        }
    }
}

scanInterface -- 扫描的接口
injectedClass -- 注入的类
injectMethod -- 调用的方法
injectedPosition -- 注入的位置
injectType -- 注入类型 【string】-- 包名形式 com.dedao.juvenile.activator.DDAppActivator 【instance】 -- 实例形式 new Object()
```