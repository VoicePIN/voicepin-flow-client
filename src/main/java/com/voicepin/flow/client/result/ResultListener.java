package com.voicepin.flow.client.result;

/**
 * @author Lukasz Warzecha
 */
public interface ResultListener {

    void onNewResult(VerifyResult result);

    void onError(Throwable throwable);

}
