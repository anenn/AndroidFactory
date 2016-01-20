package com.anenn.core.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;


/**
 * Created by Anenn on 16-1-19.
 */
public class CustomAsyncHttpClient {
    private static AsyncHttpClient client;

    public static synchronized AsyncHttpClient createClient(Context context) {
        if (client == null) {
            AsyncHttpClient client = new AsyncHttpClient();
            PersistentCookieStore cookieStore = new PersistentCookieStore(context);
            client.setCookieStore(cookieStore);
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new CustomX509TrustManager()},
                        new SecureRandom());
                SSLSocketFactory ssf = new CustomSSLSocketFactory(sslContext);
                ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme scheme = new Scheme("https", ssf, 443);
                client.getHttpClient().getConnectionManager().getSchemeRegistry()
                        .register(scheme);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {
//                // We initialize a default Keystore
//                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                // We load the KeyStore
//                trustStore.load(null, null);
//                // We initialize a new SSLSocketFacrory
//                MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
//                // We set that all host names are allowed in the socket factory
//                socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                // We initialize the Async Client
//                client = new AsyncHttpClient();
//                // We set the timeout to 30 seconds
//                // client.setTimeout(30 * 1000);
//                // We set the SSL Factory
//                client.setSSLSocketFactory(socketFactory);
//            } catch (KeyStoreException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (CertificateException e) {
//                e.printStackTrace();
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            } catch (UnrecoverableKeyException e) {
//                e.printStackTrace();
//            }
        }
        return client;
    }
}
