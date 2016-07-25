Using

```maven
   <dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-dubbo</artifactId>
    <version>1.0.0-SNAPSHOT</version>
   </dependency>
```
```spring boot

Brave brave = new Brave.Builder("myService").build();
new BraveDubboManagementBean(brave);

```
