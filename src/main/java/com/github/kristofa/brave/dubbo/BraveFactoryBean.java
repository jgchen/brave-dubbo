package com.github.kristofa.brave.dubbo;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.Sampler;
import com.github.kristofa.brave.SpanCollector;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by jack-cooper on 2017/2/20.
 */
public class BraveFactoryBean implements FactoryBean<Brave> {
    private String serviceName;

    private SpanCollector spanCollector = new LoggingSpanCollector();

    private Sampler sampler = Sampler.create(1.0f);

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setSpanCollector(SpanCollector spanCollector) {
        this.spanCollector = spanCollector;
    }

    @Override
    public Brave getObject() throws Exception {
        return new Brave.Builder(serviceName).spanCollector(spanCollector).traceSampler(sampler).build();
    }

    @Override
    public Class<?> getObjectType() {
        return Brave.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
