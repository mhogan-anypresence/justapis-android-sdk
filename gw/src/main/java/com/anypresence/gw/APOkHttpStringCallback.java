package com.anypresence.gw;


import com.squareup.okhttp.Response;

import java.io.IOException;

public abstract class APOkHttpStringCallback extends APOkHttpCallback<String> implements IAPFutureCallback<String> {

    public RequestContext<String> createRequestContext(HTTPMethod method, String url, APAndroidGateway gateway) {
        APAndroidStringRequestContext requestContext = new APAndroidStringRequestContext(method, url);
        requestContext.setGateway(gateway);
        requestContext.setCallback(this);

        return requestContext;
    }

    public String transformResponse(Response response) throws IOException {
        return response.body().string();
    }

}
