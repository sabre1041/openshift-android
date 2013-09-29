package com.openshift.android.test;

import android.test.ActivityInstrumentationTestCase2;

import com.openshift.android.activity.LoginActivity;

/**
 * Basic demonstration of an Android Integration test
 * 
 * @author Andrew Block
 *
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginActivityTest() {
        super(LoginActivity.class); 
    }

    public void testActivity() {
    	LoginActivity activity = getActivity();
        assertNotNull(activity);
    }
}

