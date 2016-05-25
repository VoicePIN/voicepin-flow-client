package com.voicepin.flow.client;


import com.voicepin.flow.client.exception.FlowParseException;

import javax.ws.rs.core.Response;

/**
 * @param <T> result entity
 * @author kodrzywolek
 */
public interface RestResponseParser<T> {
    ParsedResponse<T> parse(Response response) throws FlowParseException;
}
