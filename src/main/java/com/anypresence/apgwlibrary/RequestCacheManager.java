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

    public String getFromCache(String urlRequest) {
        return cache.get(urlRequest);
    }

    public void putIntoCache(String urlRequest, String value) {
        cache.put(urlRequest, value);
    }
}
