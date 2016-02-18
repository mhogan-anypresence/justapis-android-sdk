package com.anypresence.gw;


class APAndroidStringRequestContext extends StringRequestContext {

    APAndroidGateway mGateway;

    public APAndroidStringRequestContext(HTTPMethod requestMethod, String url) {
        super(requestMethod, url);
    }

    public void setGateway(APAndroidGateway mGateway) {
        this.mGateway = mGateway;
    }

}
