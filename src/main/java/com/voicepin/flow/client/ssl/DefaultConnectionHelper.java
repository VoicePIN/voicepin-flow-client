package com.voicepin.flow.client.ssl;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

/**
 * @author Lukasz Warzecha
 */
public class DefaultConnectionHelper implements SecureConnectionHelper {

    @Override
    public HostnameVerifier getHostnameVerifer() {
        return (String hostname, SSLSession session) -> true;
    }

    @Override
    public SSLContext getSSLContext() {
        try {
            return SSLContext.getDefault();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
