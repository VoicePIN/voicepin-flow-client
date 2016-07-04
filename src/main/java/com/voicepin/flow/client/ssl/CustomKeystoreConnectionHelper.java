package com.voicepin.flow.client.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Uses custom certificate keystore.
 *
 * @author mckulpa, Lukasz Warzecha
 */
public class CustomKeystoreConnectionHelper implements SecureConnectionHelper {

    private final String keystoreResourcePath;
    private final String keystorePassword;

    public CustomKeystoreConnectionHelper(String keystoreResourcePath, String keystorePassword) {
        this.keystorePassword = keystorePassword;
        this.keystoreResourcePath = keystoreResourcePath;
    }

    @Override
    public HostnameVerifier getHostnameVerifer() {
        return (String hostname, SSLSession session) -> true;
    }

    @Override
    public SSLContext getSSLContext() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream in = new FileInputStream(new File(keystoreResourcePath));
            ks.load(in, keystorePassword.toCharArray());
            in.close();
            trustManagerFactory.init(ks);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, trustManagerFactory.getTrustManagers(), null);
            return ctx;
        } catch (KeyManagementException | CertificateException |
                NoSuchAlgorithmException | KeyStoreException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
