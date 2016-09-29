package com.voicepin.flow.client;

import com.voicepin.flow.client.exception.FlowClientException;

/**
 * @author Lukasz Warzecha
 */
interface StreamingProcess {

    /**
     * Iterate through given throwable stacktrace in order to find FlowClientException.
     *
     * If that is not possible then it wraps given exception with FlowClientException.
     *
     * This method is useful during e.g. usage of CompleteableFuture which wraps
     * internalExceptions in ExecutionException.
     * 
     * @param throwable
     *
     * @return
     */
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
