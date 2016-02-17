package testapplication.android.laidbacklife.net.test;

import android.test.ActivityInstrumentationTestCase2;

import com.anypresence.gw.APAndroidGateway;
import com.anypresence.gw.TransformedResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import dagger.Provides;

public class HttpTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public HttpTest() {
        super(MainActivity.class);
    }

    public HttpTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @BeforeClass
    public void setUp() throws Exception {
        Base.start();
    }

    @AfterClass
    public void teardown() throws IOException, InterruptedException {
        Base.stop();
    }


    public void test_Mockserver() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("Foobar"));

        Request request = new Request.Builder()
                .url("http://127.0.0.1:9999/test")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Assert.assertEquals("Foobar", response.body().string());
    }

    public void test_GetFromGateway() throws IOException {
        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("foo").setResponseCode(200));

        APAndroidGateway gw = new APAndroidGateway.Builder().url("http://127.0.0.1:9999/api/foo").build(this.getActivity());

        gw.get();
        String resp = gw.readResponse().data;

        Assert.assertEquals("foo", resp);
    }


}
