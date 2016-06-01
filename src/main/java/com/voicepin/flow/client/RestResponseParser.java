package com.voicepin.flow.client;


import javax.ws.rs.core.Response;

import com.voicepin.flow.client.exception.FlowParseException;

/**
 * @author kodrzywolek
 */
public interface RestResponseParser<T> {
    T parse(Response response) throws FlowParseException;
}
