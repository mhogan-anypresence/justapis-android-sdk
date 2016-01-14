package com.anypresence.apgwlibrary;


import com.anypresence.gw.exceptions.RequestException;

/**
 * The class represents a request cache manager.
 * It uses Android's LRU cache as an in-memory cache.
 *
 * The cache is keyed by the request method and the url path, e.g.
 *
 * GET+http://localhost/api/v1/foo.json
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

    public String getFromCache(String method, String urlRequest) {
        return cache.get(constructKey(method, urlRequest));
    }

    public void putIntoCache(String method, String urlRequest, String value) {
        cache.put(constructKey(method, urlRequest), value);
    }

    private String constructKey(String method, String urlRequest) {
        return method.toUpperCase()+urlRequest.toUpperCase();
    }


}
