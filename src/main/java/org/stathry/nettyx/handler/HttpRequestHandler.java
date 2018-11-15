package org.stathry.nettyx.handler;

import org.stathry.nettyx.ResponseEntity;
import org.stathry.nettyx.SimpleHttpRequest;

/**
 * HttpRequestHandler
 * Created by dongdaiming on 2018-11-15 11:10
 */
public interface HttpRequestHandler {

    ResponseEntity handle(SimpleHttpRequest request);
}
