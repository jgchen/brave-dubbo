package com.github.kristofa.brave.dubbo.support;

import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.dubbo.DubboServerNameProvider;

/**
 * Created by chenjg on 16/8/22.
 */

/**
 *   解析dubbo Provider applicationName
 *   dubbo默认没有提供,只能标准化项目包名
 *   形如 com.reapal.appname.serverName
 */
public class DefaultServerNameProvider implements DubboServerNameProvider {
    @Override
    public String resolveServerName(RpcContext rpcContext) {
           String application = RpcContext.getContext().getUrl().getParameter("application");
           return application;
    }
}
