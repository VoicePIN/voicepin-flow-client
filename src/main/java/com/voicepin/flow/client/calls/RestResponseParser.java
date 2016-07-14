package com.voicepin.flow.client.calls;

import javax.ws.rs.core.Response;

/**
 * @author kodrzywolek
 */
@FunctionalInterface
interface RestResponseParser<T> {

    T parse(Response response);
}
