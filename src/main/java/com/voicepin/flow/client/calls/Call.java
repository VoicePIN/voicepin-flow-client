package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.RestRequest;
import com.voicepin.flow.client.RestResponseParser;

/**
 * @author mckulpa
 */
public interface Call<T> extends RestRequest, RestResponseParser<T> {

}
