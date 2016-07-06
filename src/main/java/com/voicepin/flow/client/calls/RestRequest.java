package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;

import javax.ws.rs.client.Entity;

/**
 * @author mckulpa
 */
interface RestRequest {

    Method getMethod();

    String getPath();

    Entity<?> getEntity();

    boolean isChunked();
}
