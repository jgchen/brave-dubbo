package com.github.kristofa.brave.dubbo.support;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.dubbo.DubboSpanNameProvider;

/**
 * Created by chenjg on 16/8/22.
 */
public class DefaultSpanNameProvider implements DubboSpanNameProvider {
    @Override
    public String resolveSpanName(RpcContext rpcContext) {
        String className = rpcContext.getUrl().getPath();
        String simpleName = className.substring(className.lastIndexOf(".")+1);
        return simpleName+"."+rpcContext.getMethodName();

    }
}
