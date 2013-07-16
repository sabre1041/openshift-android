package com.redhat.openshift.test;

import android.test.ActivityInstrumentationTestCase2;

import com.redhat.openshift.HelloAndroidActivity;
import com.redhat.openshift.test.*;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloAndroidActivity> {

    public HelloAndroidActivityTest() {
        super(HelloAndroidActivity.class); 
    }

    public void testActivity() {
        HelloAndroidActivity activity = getActivity();
        assertNotNull(activity);
    }
}

