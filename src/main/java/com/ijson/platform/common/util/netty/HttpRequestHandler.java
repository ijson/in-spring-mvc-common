
package com.ijson.platform.common.util.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletContext;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final DispatcherServlet servlet;
    private final ServletContext servletContext;


    public HttpRequestHandler(DispatcherServlet servlet) {
        this.servlet = servlet;
        this.servletContext = servlet.getServletConfig().getServletContext();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        log.error(e.getMessage(), e);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        boolean flag = HttpMethod.POST.equals(fullHttpRequest.getMethod())
                || HttpMethod.GET.equals(fullHttpRequest.getMethod());

        Map<String, String> parammap = getRequestParams(ctx, fullHttpRequest);
        if (flag && ctx.channel().isActive()) {
            //HTTP请求、GET/POST
            MockHttpServletResponse servletResponse = new MockHttpServletResponse();
            MockHttpServletRequest servletRequest = new MockHttpServletRequest(servletContext);
            // headers
            for (String name : fullHttpRequest.headers().names()) {
                for (String value : fullHttpRequest.headers().getAll(name)) {
                    servletRequest.addHeader(name, value);
                }
            }
            String uri = fullHttpRequest.getUri();
            uri = new String(uri.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            uri = URLDecoder.decode(uri, "UTF-8");
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
            String path = uriComponents.getPath();
            path = URLDecoder.decode(path, "UTF-8");
            servletRequest.setRequestURI(path);
            servletRequest.setServletPath(path);
            servletRequest.setMethod(fullHttpRequest.getMethod().name());

            if (uriComponents.getScheme() != null) {
                servletRequest.setScheme(uriComponents.getScheme());
            }
            if (uriComponents.getHost() != null) {
                servletRequest.setServerName(uriComponents.getHost());
            }
            if (uriComponents.getPort() != -1) {
                servletRequest.setServerPort(uriComponents.getPort());
            }

            ByteBuf content = fullHttpRequest.content();
            content.readerIndex(0);
            byte[] data = new byte[content.readableBytes()];
            content.readBytes(data);
            servletRequest.setContent(data);

            if (uriComponents.getQuery() != null) {
                String query = UriUtils.decode(uriComponents.getQuery(), "UTF-8");
                servletRequest.setQueryString(query);
            }
            if (parammap.size() > 0) {
                for (String key : parammap.keySet()) {
                    servletRequest.addParameter(UriUtils.decode(key, "UTF-8"), UriUtils.decode(parammap.get(key) == null ? "" : parammap.get(key), "UTF-8"));
                }
            }

            this.servlet.service(servletRequest, servletResponse);

            HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
            String result = servletResponse.getContentAsString();
            result = StringUtils.isEmpty(result) ? "" : result;
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
            response.headers().set("Content-Type", "text/json;charset=UTF-8");
            response.headers().set("Access-Control-Allow-Origin", "*");
            response.headers().set("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With,X-File-Name");
            response.headers().set("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
            response.headers().set("Content-Length", response.content().readableBytes());
            response.headers().set("Connection", "keep-alive");
            ChannelFuture writeFuture = ctx.writeAndFlush(response);
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 获取post请求、get请求的参数保存到map中
     */
    private Map<String, String> getRequestParams(ChannelHandlerContext ctx, HttpRequest req) {
        Map<String, String> requestParams = new HashMap<String, String>();
        // 处理get请求  
        if (req.getMethod() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
            Map<String, List<String>> parame = decoder.parameters();
            for (Entry<String, List<String>> next : parame.entrySet()) {
                requestParams.put(next.getKey(), next.getValue().get(0));
            }
        }
        // 处理POST请求
        if (req.getMethod() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
                    new DefaultHttpDataFactory(false), req);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); //
            for (InterfaceHttpData data : postData) {
                if (data.getHttpDataType() == HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        return requestParams;
    }


}
