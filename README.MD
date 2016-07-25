Using

   <dependency>
    <groupId>io.zipkin.brave</groupId>
    <artifactId>brave-dubbo</artifactId>
    <version>1.0.0-SNAPSHOT</version>
   </dependency>

Brave brave = new Brave.Builder("myService").build();
new BraveDubboManagementBean(brave);

Or

<bean id="braveBuilder" class="com.github.kristofa.brave.Brave.Builder">
    <contructor-arg value="myService" />
</bean>
<bean id="brave" factory-bean="braveBuilder" factory-method="build" />
<bean class="com.github.kristofa.brave.dubbo.BraveDubboManagementBean"
    <constructor-arg ref="brave" />
</bean>
