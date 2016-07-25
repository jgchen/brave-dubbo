package com.github.kristofa.brave.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.ClientSpanThreadBinder;
import com.twitter.zipkin.gen.Span;

/**
 * Created by chenjg on 16/7/24.
 */
@Activate(group = Constants.CONSUMER)
public class BraveConsumerFilter implements Filter {


    private static volatile Brave brave;
    private static volatile ClientRequestInterceptor clientRequestInterceptor;
    private static volatile ClientResponseInterceptor clientResponseInterceptor;
    private static volatile ClientSpanThreadBinder clientSpanThreadBinder;

    public static void setBrave(Brave brave) {
        BraveConsumerFilter.brave = brave;
        BraveConsumerFilter.clientRequestInterceptor = brave.clientRequestInterceptor();
        BraveConsumerFilter.clientResponseInterceptor = brave.clientResponseInterceptor();
        BraveConsumerFilter.clientSpanThreadBinder = brave.clientSpanThreadBinder();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        clientRequestInterceptor.handle(new DubboClientRequestAdapter(invoker,invocation));
        final Span currentClientSpan = clientSpanThreadBinder.getCurrentClientSpan();
        try{
            Result rpcResult = invoker.invoke(invocation);
            clientResponseInterceptor.handle(new DubboClientResponseAdapter(rpcResult));
            return rpcResult;
        }catch (Exception ex){
            clientResponseInterceptor.handle(new DubboClientResponseAdapter(ex));
            throw  ex;
        }finally {
            clientSpanThreadBinder.setCurrentSpan(null);
        }


    }
}
