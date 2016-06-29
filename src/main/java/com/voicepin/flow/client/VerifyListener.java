package com.voicepin.flow.client;

import com.voicepin.flow.client.result.VerifyResult;

/**
 * @author Lukasz Warzecha
 */
public interface VerifyListener {

    void onError(Throwable throwable);

    void onSuccess(VerifyResult verifyResult);

}
