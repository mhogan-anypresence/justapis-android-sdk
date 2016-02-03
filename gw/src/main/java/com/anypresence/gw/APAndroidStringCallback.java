package com.anypresence.gw;

public abstract class APAndroidStringCallback extends APAndroidCallback<String> {

    public RequestContext<String> createRequestContext(HTTPMethod method, String url, APAndroidGateway gateway) {
        APAndroidStringRequestContext requestContext = new APAndroidStringRequestContext(method, url);
        requestContext.setGateway(gateway);
        requestContext.setCallback(this);

        mMethod = method;
        mUrl = url;

        return requestContext;
    }

    @Override
    public void onResponse(String response) {
        APAndroidGateway.getCacheManager().putIntoCache(mMethod.toString(), mUrl, response);
        super.onResponse(response);
    }
}
