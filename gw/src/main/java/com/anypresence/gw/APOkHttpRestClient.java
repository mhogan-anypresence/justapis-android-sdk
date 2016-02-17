package com.anypresence.gw;

import com.anypresence.gw.exceptions.RequestException;
import com.anypresence.gw.http.IRestClient;
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
    private OkHttpClient client = new OkHttpClient();

    private ResponseFromRequest lastResponse;

    @Inject
    public APOkHttpRestClient() {
    }

    @Override
    public ResponseFromRequest readResponse() {
        return lastResponse;
    }

    @Override
    public void executeRequest(RequestContext<?> request) throws RequestException {
        String url = request.getUrl();
        Request.Builder builder = new Request.Builder()
                .url(url);

        Request req = builder.build();

        Response response = null;
        try {
            response = client.newCall(req).execute();
            String result = response.body().string();

            lastResponse = new ResponseFromRequest(200, result);
        } catch (IOException e) {
            e.printStackTrace();
            lastResponse = new ResponseFromRequest(response.code(), "");
        }
    }
}

