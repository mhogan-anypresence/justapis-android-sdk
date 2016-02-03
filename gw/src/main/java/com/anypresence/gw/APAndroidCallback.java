package com.anypresence.gw;

import com.android.volley.Response;

abstract class APAndroidCallback<T> extends com.anypresence.gw.APCallback<T> implements Response.Listener<T>  {

    protected HTTPMethod mMethod;
    protected String mUrl;

    public RequestContext<T> createRequestContext(HTTPMethod method, String url, APAndroidGateway gateway) {
        return null;
    }

    @Override
    public void onResponse(T response) {

        finished(response, null);
    }

}