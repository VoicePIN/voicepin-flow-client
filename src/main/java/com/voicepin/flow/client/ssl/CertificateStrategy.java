package com.voicepin.flow.client.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 * @author mckulpa, Lukasz Warzecha
 */
public interface CertificateStrategy {

    HostnameVerifier getHostnameVerifer();

    SSLContext getSSLContext();

}
