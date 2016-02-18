package com.anypresence.gw;

import android.content.Context;

import com.anypresence.gw.cache.ICacheManager;
import com.anypresence.gw.exceptions.RequestException;
import com.anypresence.gw.http.DefaultRestClient;
import com.anypresence.gw.http.IRestClient;

import java.util.Map;

import javax.inject.Inject;

public class APAndroidGateway {
    private APGateway mAPGateway;

    /** Volley request queue */
    private static com.android.volley.RequestQueue mRequestQueue;

    /** Cache manager */
    private static ICacheManager mCacheManager;

    private APAndroidGateway(Context context, APGateway gateway) {
        mAPGateway = gateway;
        mAPGateway.setRestClient(new APOkHttpRestClient(context));
        mAPGateway.useCaching(false);
    }

    public static CertPinningManager getCertPinningManager() {
        return CertPinningManager.getInstance();
    }

    public static com.android.volley.RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static void setRequestQueue(com.android.volley.RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    public static void setCacheManager(ICacheManager cacheManager) {
        mCacheManager = cacheManager;
    }

    public static ICacheManager getCacheManager() {
        if (mCacheManager == null) {
            mCacheManager = new APAndroidCacheManager();
        }
        return mCacheManager;
    }

    public static void stopRequestQueue() {
        mRequestQueue.stop();
    }

    public static void startRequestQueue() {
        mRequestQueue.start();
    }

    public APAndroidGateway useCaching(boolean shouldUseCaching) {
        mAPGateway.useCaching(shouldUseCaching);
        return this;
    }

    public IRestClient getRestClient() {
        return mAPGateway.getRestClient();
    }

    /**
     * Executes the request
     */
    public void execute() throws RequestException {
        execute(mAPGateway.getUrl());
    }

    public void execute(String url) throws RequestException {
        execute(url, null);
    }

    public void execute(HTTPMethod method) throws RequestException {
        execute(mAPGateway.getUrl(), method, null, null, null);
    }

    public <T> void execute(final String url, APAndroidCallback<T> callback) throws RequestException {
        execute(url, null, null, null, callback);
    }

    private <T> void execute(final String url, final HTTPMethod method, APAndroidCallback<T> callback) throws RequestException {
        execute(url, method, null, null, callback);
    }

    /**
     * @param <T>
     * @see APGateway#execute()
     * @param url
     *            relative url to connect to
     */
    private <T> void execute(final String url, final HTTPMethod method, Map<String,String> postParam, Map<String,String> headers,
                             APAndroidCallback<T> callback) throws RequestException {
        final HTTPMethod resolvedMethod = (method == null) ? mAPGateway.getMethod()
                : method;

        RequestContext<?> requestContext;

        if (callback != null) {
            requestContext = callback.createRequestContext(resolvedMethod, Utilities.updateUrl(mAPGateway.getUrl(), url), this);
        } else {
            requestContext = new APAndroidStringRequestContext(resolvedMethod, Utilities.updateUrl(mAPGateway.getUrl(), url));
            ((APAndroidStringRequestContext)requestContext).setGateway(this);
        }
        requestContext.setHeaders(headers);
        requestContext.setPostParam(postParam);
        requestContext.setGateway(this.mAPGateway);

        mAPGateway.getRestClient().executeRequest(requestContext);
    }

    /**
     * @see APGateway#post(String)
     */
    public void post() {
        try {
            execute(mAPGateway.getUrl(), HTTPMethod.POST, null);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends post request
     *
     * @param url
     *            relative url to connect to
     */
    public void post(String url, String body) {
        try {
            execute(url, HTTPMethod.POST, null);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    public <T> void post(APAndroidCallback<T> callback) {
        post(mAPGateway.getUrl(), callback);
    }

    public <T> void post(String url, APAndroidCallback<T> callback) {
        try {
            execute(url, HTTPMethod.POST, callback);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see APGateway#get(String)
     */
    public void get() {
        try {
            execute(HTTPMethod.GET);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a get request
     *
     * @param url
     *            relative url to connect to
     */
    public void get(String url) {
        try {
            execute(url, HTTPMethod.GET, null);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    public <T> void get(APAndroidCallback<T> callback) {
        get(mAPGateway.getUrl(), callback);
    }

    public <T> void get(String url, APAndroidCallback<T> callback) {
        try {
            execute(url, HTTPMethod.GET, callback);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    public ResponseFromRequest readResponse() {
        return mAPGateway.getRestClient().readResponse();
    }

    public static class Builder {
        private APGateway.Builder mBuilder;

        public Builder () {
            mBuilder = new APGateway.Builder();
        }

        public Builder url(String url) {
            mBuilder.url(url);
            return this;
        }

        public Builder method(HTTPMethod method) {
            mBuilder.method(method);
            return this;
        }

        public APAndroidGateway build(Context context) {
            return new APAndroidGateway(context, mBuilder.build());
        }
    }
}
