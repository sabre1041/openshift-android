package com.openshift.android.processor;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;


/**
 * Utility class defining constants for filtering Broadcast Messages
 * 
 * @author Andrew Block
 * @see BroadcastReceiver
 * @see IntentFilter
 *
 */
public class OpenshiftActions {
	
	public static final String LIST_DOMAINS = "com.redhat.openshift.LIST_DOMAINS";
	public static final String USER_DETAIL = "com.redhat.openshift.USER_DETAIL";
	public static final String LIST_APPLICATIONS = "com.redhat.openshift.LIST_APPLICATIONS";
	public static final String STOP_APPLICATION = "com.redhat.openshift.STOP_APPLICATION";
	public static final String START_APPLICATION = "com.redhat.openshift.START_APPLICATION";
	public static final String RESTART_APPLICATION = "com.redhat.openshift.RESTART_APPLICATION";
	public static final String DELETE_APPLICATION = "com.redhat.openshift.DELETE_APPLICATION";

}

