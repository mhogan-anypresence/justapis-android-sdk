package com.anypresence.gw;

import android.content.Context;

import com.anypresence.gw.exceptions.RequestException;
import com.anypresence.gw.http.IRestClient;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Rest client using OKHttp
 *
 */
public class APOkHttpRestClient implements IRestClient {
    private OkHttpClient client;

    private ResponseFromRequest lastResponse;


    @Inject
    public APOkHttpRestClient() {
        client = new OkHttpClient();
    }

    public APOkHttpRestClient(Context context) {
        // Setup caching
        int cacheSize = 1024 * 1024;
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        client = new OkHttpClient();
        client.setCache(cache);
    }

    public OkHttpClient getOkHttpClient() {
        return client;
    }

    public Cache getCache() {
        return client.getCache();
    }

    @Override
    public ResponseFromRequest readResponse() {
        return lastResponse;
    }

    @Override
    public void executeRequest(RequestContext<?> request) throws RequestException {
        String url = request.getUrl();
        CacheControl cacheControl;
        Request.Builder builder = new Request.Builder().url(url);

        if (!request.getGateway().getUseCaching()) {
            // Do not use caching
            cacheControl = new CacheControl.Builder().noStore().build();
            builder = builder.cacheControl(cacheControl);
        }

        Request req = builder.build();

        final APAndroidStringCallback callback = (APAndroidStringCallback) ((APAndroidStringRequestContext) request).getCallback();

        Response response = null;
        if (callback == null) {
            try {
                response = client.newCall(req).execute();

                String result = response.body().string();

                lastResponse = new ResponseFromRequest(response.code(), result);
            } catch (IOException e) {
                e.printStackTrace();
                lastResponse = new ResponseFromRequest(response.code(), "");
            }
        } else {
            client.newCall(req).enqueue(callback);
        }

    }
}

