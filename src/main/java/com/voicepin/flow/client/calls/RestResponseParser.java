package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.exception.FlowParseException;

import javax.ws.rs.core.Response;

/**
 * @author kodrzywolek
 */
@FunctionalInterface
interface RestResponseParser<T> {

    T parse(Response response) throws FlowParseException;
}
