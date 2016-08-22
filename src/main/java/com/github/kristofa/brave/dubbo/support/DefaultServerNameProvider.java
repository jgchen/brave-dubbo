package com.github.kristofa.brave.dubbo.support;

import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.dubbo.DubboServerNameProvider;

/**
 * Created by chenjg on 16/8/22.
 */

/**
 *   解析dubbo Provider applicationName
 *   dubbo默认没有提供,只能标准化项目包名
 *   形如 com.company.bu.serverName.api.XXXX
 */
public class DefaultServerNameProvider implements DubboServerNameProvider {
    @Override
    public String resolveServerName(RpcContext rpcContext) {
        String interfaceName= rpcContext.getUrl().getPath();
        String packageName =interfaceName.substring(0,interfaceName.lastIndexOf(".api."));
        String  serverName =  packageName.substring(packageName.lastIndexOf(".")+1);
        return serverName;
    }
}
