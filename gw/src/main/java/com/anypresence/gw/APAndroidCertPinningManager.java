package com.anypresence.gw;

import android.util.Base64;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

public class APAndroidCertPinningManager {
    private static APAndroidCertPinningManager manager;

    private Map<String,String> certs = new HashMap<>();

    private APAndroidCertPinningManager() {}

    public static APAndroidCertPinningManager getInstance() {
        if (manager == null) {
            manager = new APAndroidCertPinningManager();
        }
        return manager;
    }


    /**
     * Adds cert for provided hostname.
     *
     * Some reading material https://www.imperialviolet.org/2011/05/04/pinning.html
     *
     * Example:
     * for google.com
     * #add("google.com", "sha1/qANMQh2fy6tyyjS9qEjosxOLe1w=")
     *
     * @param hostname
     * @param sha1 base64 Subject Public Key Info is needed that starts with 'sha1/'
     */
    public void add(String hostname, String sha1) {
        certs.put(hostname, sha1);
    }

    /**
     * Adds cert for provided hostname.
     *
     * @param hostname
     * @param data the certficate file as a byte array
     */
    public void add(String hostname, byte[] data) {
        try {
            certs.put(hostname, rehashCertFromByteArray(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    public Map<String,String> getCerts() {
        return certs;
    }

    public void addAllCertsToClient(OkHttpClient client) {
        CertificatePinner.Builder builder = new CertificatePinner.Builder();

        for (Map.Entry<String, String> entry : certs.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        client.setCertificatePinner(builder.build());
    }

    private String rehashCertFromByteArray(byte[] data) throws NoSuchAlgorithmException, CertificateException {
        X509Certificate cert = X509Certificate.getInstance(data);
        byte[] pubKey = cert.getPublicKey().getEncoded();

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        final byte[] hash = digest.digest(pubKey);

        return "sha1/" + Base64.encodeToString(hash, Base64.NO_PADDING);
    }
}
