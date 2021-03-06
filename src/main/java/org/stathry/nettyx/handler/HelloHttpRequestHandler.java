package org.stathry.nettyx.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stathry.nettyx.ResponseEntity;
import org.stathry.nettyx.SimpleHttpRequest;
import org.stathry.nettyx.util.ErrorEnum;

import java.util.Map;
import java.util.UUID;

/**
 * HelloHttpRequestHandler
 * Created by dongdaiming on 2018-11-15 12:06
 */

@Component("helloHttpRequestHandler")
public class HelloHttpRequestHandler implements HttpRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloHttpRequestHandler.class);
    @Override
    public ResponseEntity handle(SimpleHttpRequest request) {
        LOGGER.info("transId {}, params {}.", request.getSessionId(), JSON.toJSONString(request.getParams()));
        ResponseEntity res = new ResponseEntity(ErrorEnum.SUCCESS);
        res.setSessionId(request.getSessionId());
        res.setBizNo(UUID.randomUUID().toString());

        JSONObject json = new JSONObject((Map)request.getParams());
        json.put("name", "hello world");
        json.put("year", "2018");
        res.setData(json);

        return res;
    }
}
