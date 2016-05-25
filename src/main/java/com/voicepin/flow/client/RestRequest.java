package com.voicepin.flow.client;

import javax.ws.rs.client.Entity;

/**
 * @author mckulpa
 */
public interface RestRequest {
    Method getMethod();

    String getPath();

    Entity<?> getEntity();

    boolean isChunked();
}
