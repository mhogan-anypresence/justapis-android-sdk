package testapplication.android.laidbacklife.net.test;

import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.mockito.Mock;

import java.io.IOException;

class Base {
    private static volatile boolean started = false;

    private static MockWebServer mMockWebServer;

    public synchronized  static void start() throws IOException {
        if (!started) {
            mMockWebServer = new MockWebServer();
            mMockWebServer.start(9999);
            started = true;
        }
    }

    public synchronized  static void stop() throws IOException {
        mMockWebServer.shutdown();
        started = false;
    }

    public static synchronized MockWebServer getMockWebServer() {
        return mMockWebServer;
    }
}
