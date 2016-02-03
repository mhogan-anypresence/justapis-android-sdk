package testapplication.android.laidbacklife.net.test;


import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;
import testapplication.android.laidbacklife.net.test.*;


public class APGatewayTest extends ActivityInstrumentationTestCase2<MainActivity> {

    /**
     *  Need a default constructor or the test will not run
     */
    public APGatewayTest() {
        super(MainActivity.class);
    }


    public APGatewayTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    public void testActivityExists() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }


    public void testConnection() {
//        MainActivity activity = getActivity();
//
//        final EditText nameEditText =
//                (EditText) activity.findViewById(R.id.greet_edit_text);

        Assert.assertTrue(false);


    }

}
