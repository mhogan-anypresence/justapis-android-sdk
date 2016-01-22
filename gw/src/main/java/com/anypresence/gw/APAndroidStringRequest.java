package com.anypresence.gw;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

class APAndroidStringRequest extends StringRequest {
    private Map<String,String> mHeaders;
    private Map<String,String> mParams;

    public APAndroidStringRequest(int method, String url, Map<String,String> headers, Map<String, String> params,
                                  Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mHeaders = headers;
        mParams = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> headers = super.getHeaders();
        if (mHeaders != null) {
            headers.putAll(mHeaders);
        }
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String> params =  super.getParams();
        if (mParams != null) {
            params.putAll(mParams);
        }
        return params;
    }
}
