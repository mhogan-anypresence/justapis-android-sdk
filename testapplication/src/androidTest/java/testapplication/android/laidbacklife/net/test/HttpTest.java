package testapplication.android.laidbacklife.net.test;

import android.test.ActivityInstrumentationTestCase2;

import com.anypresence.gw.APAndroidGateway;
import com.anypresence.gw.APOkHttpRestClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.mockwebserver.MockResponse;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.anypresence.gw.APAndroidStringCallback;

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


    public void test_Mockserver() throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();

        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("Foobar"));

        Request request = new Request.Builder()
                .url("http://127.0.0.1:9999/test")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Assert.assertEquals("Foobar", response.body().string());

        Base.getMockWebServer().takeRequest();
    }

    // Gets

    public void test_GetWithVariousCodes() throws IOException, InterruptedException {
        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("foo").setResponseCode(201));

        APAndroidGateway gw = new APAndroidGateway.Builder().url("http://127.0.0.1:9999/api/foo").build(this.getActivity());

        gw.get();
        Assert.assertEquals(201, gw.readResponse().statusCode);

        Base.getMockWebServer().takeRequest();

        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("foo").setResponseCode(400));

        gw.get();
        Assert.assertEquals(400, gw.readResponse().statusCode);

        Base.getMockWebServer().takeRequest();
    }

    public void test_GetFromGateway() throws IOException, InterruptedException {
        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("foo").setResponseCode(200));

        APAndroidGateway gw = new APAndroidGateway.Builder().url("http://127.0.0.1:9999/api/foo").build(this.getActivity());

        gw.get();
        String resp = gw.readResponse().data;

        Assert.assertEquals(200, gw.readResponse().statusCode);
        Assert.assertEquals("foo", resp);

        Assert.assertEquals("/api/foo", Base.getMockWebServer().takeRequest(2, TimeUnit.SECONDS).getPath());
    }

    public void test_Get_UpdateRelativePath() throws IOException, InterruptedException {
        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("foo").setResponseCode(200));

        APAndroidGateway gw = new APAndroidGateway.Builder().url("http://127.0.0.1:9999/api/foo").build(this.getActivity());

        gw.get("bar");
        String resp = gw.readResponse().data;

        Assert.assertEquals(200, gw.readResponse().statusCode);
        Assert.assertEquals("foo", resp);

        Assert.assertEquals("/api/foo/bar", Base.getMockWebServer().takeRequest(2, TimeUnit.SECONDS).getPath());
    }

    public void test_GetAsync() throws IOException, InterruptedException {
        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("async time").setResponseCode(200));

        APAndroidGateway gw = new APAndroidGateway.Builder().url("http://127.0.0.1:9999/api/foo").build(this.getActivity());
        final CountDownLatch endSignal = new CountDownLatch(1);

        gw.get(new APAndroidStringCallback() {

            @Override
            public void finished(String object, Throwable ex) {
                Assert.assertNull(ex);
                Assert.assertEquals("async time", object);
                endSignal.countDown();
            }
        });
        endSignal.await();

        Assert.assertEquals("/api/foo", Base.getMockWebServer().takeRequest(2, TimeUnit.SECONDS).getPath());
    }

    // Posts

    public void test_PostFromGateway() throws IOException, InterruptedException {
        Base.getMockWebServer().enqueue(new MockResponse()
                .setBody("foo").setResponseCode(200));

        APAndroidGateway gw = new APAndroidGateway.Builder().url("http://127.0.0.1:9999/api/foo").build(this.getActivity());

        gw.post();
        String resp = gw.readResponse().data;
        Assert.assertEquals(200, gw.readResponse().statusCode);

        Assert.assertEquals("foo", resp);
        Assert.assertEquals("/api/foo", Base.getMockWebServer().takeRequest().getPath());
    }


    // Caching

    public void test_Cache() throws InterruptedException {
        Base.getMockWebServer().enqueue(new MockResponse()
                .addHeader("Cache-Control: max-age=60")
                .setBody("foo").setResponseCode(200));

        APAndroidGateway gw = new APAndroidGateway.Builder().url("http://127.0.0.1:9999/api/foo").build(this.getActivity());

        gw.useCaching(true).get();

        String resp = gw.readResponse().data;
        Assert.assertEquals(200, gw.readResponse().statusCode);

        Base.getMockWebServer().takeRequest();

        Assert.assertTrue(((APOkHttpRestClient) gw.getRestClient()).getCache().getRequestCount() == 1);
        Assert.assertTrue(((APOkHttpRestClient) gw.getRestClient()).getCache().getNetworkCount() == 1);
        Assert.assertTrue(((APOkHttpRestClient) gw.getRestClient()).getCache().getHitCount() == 0);

        // Test the cache
        gw.useCaching(true).get();
        resp = gw.readResponse().data;
        Assert.assertEquals(200, gw.readResponse().statusCode);

        Assert.assertTrue(((APOkHttpRestClient) gw.getRestClient()).getCache().getRequestCount() == 2);
        Assert.assertTrue(((APOkHttpRestClient) gw.getRestClient()).getCache().getNetworkCount() == 1);
        Assert.assertTrue(((APOkHttpRestClient) gw.getRestClient()).getCache().getHitCount() == 1);

    }


}
