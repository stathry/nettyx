package org.stathry.nettyx;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * SimpleHttpRequest
 * Created by dongdaiming on 2018-11-15 10:31
 */
public class SimpleHttpRequest {

    private HttpRequest request;
    private String sessionId = UUID.randomUUID().toString();
    private Date timestamp = new Date();
    private String path;
    private Map<String, String> params;

    public SimpleHttpRequest(HttpRequest request) {
        this.request = request;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "SimpleHttpRequest{" +
                "sessionId='" + sessionId + '\'' +
                ", timestamp=" + timestamp +
                ", path='" + path + '\'' +
                ", params=" + params +
                '}';
    }
}
