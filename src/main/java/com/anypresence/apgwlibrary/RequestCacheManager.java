package com.anypresence.apgwlibrary;


import com.anypresence.gw.exceptions.RequestException;

/**
 *
 */
public class RequestCacheManager {
    private InMemoryCache cache;

    private RequestCacheManager requestCacheManager;

    private RequestCacheManager() {}

    public RequestCacheManager getInstance() {
        if (requestCacheManager == null) {
            requestCacheManager = new RequestCacheManager();
        }

        return requestCacheManager;
    }

    public String getFromCache(String key) {
        return cache.get(key);
    }

    public void putIntoCache(String key, String value) {
        cache.put(key, value);
    }
}
