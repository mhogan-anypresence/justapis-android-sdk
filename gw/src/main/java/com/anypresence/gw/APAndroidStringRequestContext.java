package com.anypresence.gw;


class APAndroidStringRequestContext extends StringRequestContext {

    APAndroidGateway mGateway;

    private APAndroidStringCallback callback;

    public APAndroidStringRequestContext(HTTPMethod requestMethod, String url) {
        super(requestMethod, url);
    }

    public void setGateway(APAndroidGateway mGateway) {
        this.mGateway = mGateway;
    }

    /** callback to handle the response */
    public APAndroidStringCallback getCallback() {
        return callback;
    }

    public void setCallback(APAndroidStringCallback callback) {
        this.callback = callback;
    }
}
