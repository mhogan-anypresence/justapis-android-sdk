package com.anypresence.gw;

import android.util.Base64;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

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
     * Adds cert for provided hostname
     *
     * a base64 Subject Public Key Info is needed that starts with 'sha1/'
     *
     * Some reading material https://www.imperialviolet.org/2011/05/04/pinning.html
     *
     * Example:
     * for google.com
     * #add("google.com", "sha1/qANMQh2fy6tyyjS9qEjosxOLe1w=")
     *
     * @param hostname
     * @param sha1
     */
    public void add(String hostname, String sha1) {
        certs.put(hostname, sha1);
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
}
