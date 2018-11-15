package org.stathry.nettyx.handler;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.stathry.nettyx.HttpRequestDispatcher;
import org.stathry.nettyx.ResponseEntity;
import org.stathry.nettyx.SimpleHttpRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpChannelHandler
 * Created by dongdaiming on 2018-11-14 18:30
 */
@Component
public class HttpChannelHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpChannelHandler.class);

    @Qualifier("httpRequestDispatcher")
    @Autowired
    private HttpRequestDispatcher httpRequestDispatcher;

    private static  final Charset CHARSET = Charset.forName("utf-8");

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = (HttpRequest) e.getMessage();
        ResponseEntity entity = httpRequestDispatcher.dispatch(castRequest(request));

        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        ChannelBuffer buffer=new DynamicChannelBuffer(10240);
        buffer.writeBytes(JSON.toJSONString(entity).getBytes("UTF-8"));
        response.setContent(buffer);
        HttpHeaders headers = response.headers();
        headers.add("Content-Type", "text/html; charset=UTF-8");
        headers.add("Content-Length", response.getContent().writerIndex());
        Channel ch = e.getChannel();
        ch.write(response);
        ch.disconnect();
        ch.close();
    }

    private SimpleHttpRequest castRequest(HttpRequest request) {
        SimpleHttpRequest  req = new SimpleHttpRequest(request);

        req.setPath(parseRequestPath(request.getUri()));
        req.setParams(parseParams(request));
        return req;
    }

    private Map<String, String> parseParams(HttpRequest request) {
        Map<String, String> map = new HashMap<>();

        HttpMethod method = request.getMethod();
        if (HttpMethod.POST.equals(method)) {
            String ct = request.headers().get(HttpHeaders.Names.CONTENT_TYPE);
            if(ct.equals("application/x-www-form-urlencoded") || ct.contains("multipart/form-data")) {
                parserFormParams(request, map);
            }
        } else if (HttpMethod.GET.equals(method)) {
            String uri = request.getUri();
            int i;
            String params = (i = uri.indexOf('?')) == -1 ? "" : uri.substring(i + 1);
            if (StringUtils.isNotBlank(params)) {
                String[] arr = params.split("&");
                String[] kv;
                for (String kvs : arr) {
                    kv = kvs.split("=");
                    map.put(kv[0], kv[1]);
                }
            }
        } else {
            throw new UnsupportedOperationException(method.getName());
        }
        return map;
    }

    private void parserFormParams(HttpRequest request, Map<String,String> map) {
        try {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request, CHARSET);
            List<InterfaceHttpData> dataList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : dataList) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    Attribute attribute = (Attribute) data;
                    String value = attribute.getValue();
                    map.put(data.getName(), value);
                }
            }
        } catch (Exception e) {
            LOGGER.error("parse form params error.", e);
        }
    }

    private String parseRequestPath(String uri) {
        if(uri == null || uri.length() <= 1) {
            return "";
        }
        String path = uri.substring(1);
        int ei = path.indexOf('?');
        ei = ei == -1 ? path.length() : ei;
        path = path.substring(0, ei);
        return path;
    }

    private void simpleHandle(MessageEvent e) throws UnsupportedEncodingException {
        HttpRequest request = (HttpRequest) e.getMessage();

        LOGGER.info("http request uri {}, method {}, post data {}.", request.getUri(), request.getMethod().getName(),
                new String(request.getContent().array(), "utf-8"));

        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        ChannelBuffer buffer=new DynamicChannelBuffer(2048);
        buffer.writeBytes("hello! 中国".getBytes("UTF-8"));
        response.setContent(buffer);
        HttpHeaders headers = response.headers();
        headers.add("Content-Type", "text/html; charset=UTF-8");
        headers.add("Content-Length", response.getContent().writerIndex());
        Channel ch = e.getChannel();
        // Write the initial line and the header.
        ch.write(response);
        ch.disconnect();
        ch.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
        e.getChannel().close();
    }
}
