package com.anypresence.gw;

public abstract class APAndroidStringCallback extends APAndroidCallback<String> {

    public RequestContext<String> createRequestContext(HTTPMethod method, String url, APAndroidGateway gateway) {
        APAndroidStringRequestContext requestContext = new APAndroidStringRequestContext(method, url);
        requestContext.setGateway(gateway);
        requestContext.setCallback(this);

        return requestContext;
    }



}
