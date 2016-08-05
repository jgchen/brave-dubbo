package com.github.kristofa.brave.dubbo;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.internal.Nullable;
import com.twitter.zipkin.gen.Endpoint;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by chenjg on 16/7/24.
 */
public class DubboClientRequestAdapter implements ClientRequestAdapter {
    private Invoker<?> invoker;
    private Invocation invocation;

    public DubboClientRequestAdapter(Invoker<?> invoker, Invocation invocation) {
        this.invoker = invoker;
        this.invocation = invocation;
    }

    @Override
    public String getSpanName() {
        return invoker.getInterface().getSimpleName()+"."+invocation.getMethodName();
    }

    @Override
    public void addSpanIdToRequest(@Nullable SpanId spanId) {
        String application = RpcContext.getContext().getUrl().getParameter("application");
        RpcContext.getContext().setAttachment("clientName", application);
        if (spanId == null) {
            RpcContext.getContext().setAttachment("sampled", "0");
        }else{
            RpcContext.getContext().setAttachment("traceId", IdConversion.convertToString(spanId.traceId));
            RpcContext.getContext().setAttachment("spanId", IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                RpcContext.getContext().setAttachment("parentId", IdConversion.convertToString(spanId.parentId));
            }
        }
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        return Collections.singletonList(KeyValueAnnotation.create("url", RpcContext.getContext().getUrl().toString()));
    }

    @Override
    public Endpoint serverAddress() {
        InetSocketAddress inetSocketAddress = RpcContext.getContext().getRemoteAddress();
        String ipAddr = RpcContext.getContext().getUrl().getIp();
        String serverName = resolverServerName();
        return Endpoint.create(serverName, IPConversion.convertToInt(ipAddr),inetSocketAddress.getPort());
    }

    /**
     *
     * com.xxx.bu.serverName.api.XXXX
     * 公司应用标准化后ServerName从interfaceName中解析
     */
    private  String resolverServerName(){
       String interfaceName= invoker.getInterface().getName();
       String packageName =interfaceName.substring(0,interfaceName.lastIndexOf(".api."));
       String  serverName =  packageName.substring(packageName.lastIndexOf(".")+1);
       return serverName;
    }


}
