package com.voicepin.flow.client;

import com.voicepin.flow.client.exception.FlowClientException;

/**
 * @author Lukasz Warzecha
 */
interface StreamingProcess {

    default FlowClientException getParentException(Throwable throwable) {

        if (throwable instanceof FlowClientException) {
            return (FlowClientException) throwable;
        }

        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
            if (cause instanceof FlowClientException) {
                return (FlowClientException) cause;
            }
        }
        return new FlowClientException("Flow client error", throwable.getCause());
    }

}
