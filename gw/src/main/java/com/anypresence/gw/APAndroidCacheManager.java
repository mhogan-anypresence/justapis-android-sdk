package com.anypresence.gw;


import com.android.volley.Cache;
import com.anypresence.gw.cache.ICacheManager;

import java.io.UnsupportedEncodingException;

public class APAndroidCacheManager implements ICacheManager {

    @Override
    public void putIntoCache(String requestMethod, String url, String result) {
        Cache.Entry entry = new Cache.Entry();
        try {
            entry.data = result.getBytes("UTF-8");

            if (APAndroidGateway.getRequestQueue().getCache() != null) {
                APAndroidGateway.getRequestQueue().getCache().put(createKey(requestMethod, url), entry);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFromCache(String requestMethod, String url) {
        Cache.Entry entry = APAndroidGateway.getRequestQueue().getCache().get(createKey(requestMethod, url));


        if (entry != null) {
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                return data;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        return "";
    }

    public String createKey(String requestMethod, String url) {
        return requestMethod.toUpperCase() + "+" + url;
    }
}
