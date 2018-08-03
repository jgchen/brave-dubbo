package com.github.kristofa.brave.dubbo;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.dubbo.support.DefaultSpanNameProvider;
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
	private final static DubboSpanNameProvider spanNameProvider = new DefaultSpanNameProvider();

	public DubboClientRequestAdapter(Invoker<?> invoker, Invocation invocation) {
		this.invoker = invoker;
		this.invocation = invocation;
	}

	@Override
	public String getSpanName() {
		return spanNameProvider.resolveSpanName(RpcContext.getContext());
	}

	@Override
	public void addSpanIdToRequest(@Nullable SpanId spanId) {
		if ("com.alibaba.dubbo.monitor.MonitorService"
				.equalsIgnoreCase(RpcContext.getContext().getUrl()
						.getParameter("interface"))) {
			SpanId.builder().sampled(false).build();
			return;
		}

		String application = RpcContext.getContext().getUrl()
				.getParameter("application");
		RpcContext.getContext().setAttachment("clientName", application);

		if (spanId == null) {
			RpcContext.getContext().setAttachment("sampled", "0");
		} else {
			RpcContext.getContext().setAttachment("traceId",
					IdConversion.convertToString(spanId.traceId));
			RpcContext.getContext().setAttachment("spanId",
					IdConversion.convertToString(spanId.spanId));
			if (spanId.nullableParentId() != null) {
				RpcContext.getContext().setAttachment("parentId",
						IdConversion.convertToString(spanId.parentId));
			}
		}
	}

	@Override
	public Collection<KeyValueAnnotation> requestAnnotations() {
		return Collections.singletonList(KeyValueAnnotation.create("url",
				RpcContext.getContext().getUrl().toString()));
	}

	@Override
	public Endpoint serverAddress() {
		InetSocketAddress inetSocketAddress = RpcContext.getContext()
				.getRemoteAddress();
		String ipAddr = RpcContext.getContext().getUrl().getIp();
		String serverName = null;
		return Endpoint.create(serverName, IPConversion.convertToInt(ipAddr),
				inetSocketAddress.getPort());
	}

}
