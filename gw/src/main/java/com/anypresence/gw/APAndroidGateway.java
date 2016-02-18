package com.anypresence.gw;

import android.content.Context;

import com.anypresence.gw.exceptions.RequestException;
import com.anypresence.gw.http.IRestClient;

import java.util.Map;

public class APAndroidGateway {
    private APGateway mAPGateway;

    private APAndroidGateway(Context context, APGateway gateway) {
        mAPGateway = gateway;
        mAPGateway.setRestClient(new APOkHttpRestClient(context));
        mAPGateway.useCaching(false);
    }

    public static APAndroidCertPinningManager getCertPinningManager() {
        return APAndroidCertPinningManager.getInstance();
    }

    public APAndroidGateway useCaching(boolean shouldUseCaching) {
        mAPGateway.useCaching(shouldUseCaching);
        return this;
    }

    public IRestClient getRestClient() {
        return mAPGateway.getRestClient();
    }

    public void setUseCertPinning(boolean useCertPinning) {
        mAPGateway.setUseCertPinning(useCertPinning);

        getCertPinningManager().addAllCertsToClient(((APOkHttpRestClient) getRestClient()).getOkHttpClient());
    }

    /**
     * Executes the request
     */
    public void execute() throws RequestException {
        execute(mAPGateway.getUrl());
    }

    public void execute(String url) throws RequestException {
        execute(url, null, null, null, null);
    }

    public void execute(String url, final HTTPMethod method) throws RequestException {
        execute(url, method, null, null, null);
    }

    public void execute(HTTPMethod method) throws RequestException {
        execute(mAPGateway.getUrl(), method, null, null, null);
    }

    public <T> void execute(final String url, APAndroidCallback<T> callback) throws RequestException {
        execute(url, null, null, null, callback);
    }

    public <T> void execute(final String url, final HTTPMethod method, APAndroidCallback<T> callback) throws RequestException {
        execute(url, method, null, null, callback);
    }

    public <T> void execute(final String url, final HTTPMethod method, Map<String,String> headers, APAndroidCallback<T> callback) throws RequestException {
        execute(url, method, null, headers, callback);
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
    public void post(String url, Map<String,String> body) {
        try {
            execute(url, HTTPMethod.POST, body, null, null);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends post request
     *
     * @param callback
     * @param <T>
     */
    public <T> void post(APAndroidCallback<T> callback) {
        post(mAPGateway.getUrl(), callback);
    }

    /**
     * Sends post request
     *
     * @param url
     * @param callback
     * @param <T>
     */
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

    /**
     * Sends a get request
     *
     * @param callback
     * @param <T>
     */
    public <T> void get(APAndroidCallback<T> callback) {
        get(mAPGateway.getUrl(), callback);
    }

    /**
     * Sends a get request
     *
     * @param url
     * @param callback
     * @param <T>
     */
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
