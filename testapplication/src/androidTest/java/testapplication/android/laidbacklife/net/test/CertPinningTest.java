package testapplication.android.laidbacklife.net.test;

import android.test.ActivityInstrumentationTestCase2;

import com.anypresence.gw.APAndroidGateway;
import com.squareup.okhttp.mockwebserver.MockResponse;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class CertPinningTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public CertPinningTest() {
        super(MainActivity.class);
    }

    public CertPinningTest(Class<MainActivity> activityClass) {
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

    public void test_CertPinning() throws InterruptedException {
        APAndroidGateway.getCertPinningManager().add("google.com", "sha1/qANMQh2fy6tyyjS9qEjosxOLe1w=");
        APAndroidGateway gw = new APAndroidGateway.Builder().url("https://google.com").build(this.getActivity());

        gw.setUseCertPinning(true);
    }

}
