package com.anypresence.gw;

import android.content.Context;


import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;


import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.anypresence.gw.exceptions.RequestException;
import com.anypresence.gw.http.*;
import com.android.volley.*;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class APAndroidRestClient implements IRestClient {
    private WeakReference<Context> mContext;
    private Cache mCache;

    private ResponseFromRequest lastResponse;

    public APAndroidRestClient(Context context) {
        this.mContext = new WeakReference<Context>(context);
        if ((mContext.get() != null)) {
            mCache = new DiskBasedCache(mContext.get().getCacheDir(), 1024 * 1024); // 1MB cap
        }
    }

    public APAndroidRestClient() {
    }

    private void openConnection(RequestContext<?> requestContext) {

        Network network = new BasicNetwork(new HurlStack());

        if (APAndroidGateway.getRequestQueue() == null) {
            APAndroidGateway.setRequestQueue(new RequestQueue(mCache, network));
            APAndroidGateway.getRequestQueue().start();
            APAndroidGateway.getRequestQueue();
        }

        // Formulate the request and handle the response.
        int requestMethod;
        switch(requestContext.getMethod().toString().toUpperCase()) {
            case "POST":
                requestMethod = Request.Method.POST;
                break;
            case "PUT":
                requestMethod = Request.Method.PUT;
                break;
            case "DELETE":
                requestMethod = Request.Method.DELETE;
                break;
            default:
                requestMethod = Request.Method.GET;
        }

        final APAndroidStringCallback callback = ((APAndroidStringRequestContext) requestContext).getCallback();

        if (callback == null) {
            Config.getLogger().log("Connecting to endpoint synchronously");
            // Blocking call
            RequestFuture<String> future = RequestFuture.newFuture();
            APAndroidStringRequest stringRequest = new APAndroidStringRequest(requestMethod, requestContext.getUrl(), requestContext.getHeaders(), requestContext.getPostParam(), future,
                    future
            );

            if (requestContext.getGateway() != null && requestContext.getGateway().getUseCaching()) {
                stringRequest.setShouldCache(true);
            }
            // Add the request to the request queue
            APAndroidGateway.getRequestQueue().add(stringRequest);
            try {
                String res = future.get(30, TimeUnit.SECONDS);
                lastResponse = new ResponseFromRequest(200, res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        } else {
            APAndroidStringRequest stringRequest = new APAndroidStringRequest(requestMethod, requestContext.getUrl(), requestContext.getHeaders(), requestContext.getPostParam(), callback,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.finished(null, error);
                        }
                    }
            );

            if (requestContext.getGateway() != null && requestContext.getGateway().getUseCaching()) {
                stringRequest.setShouldCache(true);
            }

            // Add the request to the request queue
            APAndroidGateway.getRequestQueue().add(stringRequest);
        }
    }


    @Override
    public ResponseFromRequest readResponse() {
        return lastResponse;
    }

    @Override
    public void executeRequest(RequestContext<?> request) throws RequestException {
        openConnection(request);
    }

}
