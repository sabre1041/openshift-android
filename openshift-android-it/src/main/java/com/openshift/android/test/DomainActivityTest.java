package com.openshift.android.test;

import android.test.ActivityInstrumentationTestCase2;

import com.openshift.android.activity.DomainActivity;

public class DomainActivityTest extends ActivityInstrumentationTestCase2<DomainActivity> {

    public DomainActivityTest() {
        super(DomainActivity.class); 
    }

    public void testActivity() {
    	DomainActivity activity = getActivity();
        assertNotNull(activity);
    }
}

