Fork自 https://github.com/jgchen/brave-dubbo

做了一定优化,修正了服务名不准的bug,过滤监了控报文,brave bean不写serviceName

使用步骤：

### maven添加该项目jar包（自己打包安装到本地仓库）
```xml
        <dependency>
            <groupId>io.zipkin.brave</groupId>
            <artifactId>brave-dubbo</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

### spring中配置
    
   * 打印日志方式
```xml
        <bean id="brave" class="com.github.kristofa.brave.dubbo.BraveFactoryBean"  p:zipkinHost="" p:rate="1.0" />
```
   * http方式
```xml
        <bean id="brave" class="com.github.kristofa.brave.dubbo.BraveFactoryBean"  p:zipkinHost="http://10.168.16.111:9411/" p:rate="1.0" />
```



### 参考文档:
[http://www.tuicool.com/articles/f2qAZnZ](http://www.tuicool.com/articles/f2qAZnZ)
