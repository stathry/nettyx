package org.stathry.nettyx;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.nettyx.handler.HttpRequestHandler;
import org.stathry.nettyx.util.ErrorEnum;

import java.util.Map;

/**
 * HttpRequestDispatcher
 * Created by dongdaiming on 2018-11-15 10:45
 */

public class HttpRequestDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestDispatcher.class);

    private Map<String, HttpRequestHandler> handlerMap;

    public ResponseEntity dispatch(SimpleHttpRequest request) {
        LOGGER.info("request path '{}', method {}, sessionId {}, params:{}", request.getPath(),
                request.getRequest().getMethod().getName(), request.getSessionId(), JSON.toJSONString(request.getParams()));
        HttpRequestHandler handler = handlerMap.get(request.getPath());

        ResponseEntity entity;
        if(handler != null) {
            entity = handler.handle(request);
        } else {
            entity = new ResponseEntity(ErrorEnum.PARAMETER_INVALID);
        }

        LOGGER.info("sessionId {}, response data:{}", request.getSessionId(), JSON.toJSONString(request.getParams()));
        return entity;
    }

    public void setHandlerMap(Map<String, HttpRequestHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }
}
