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
	
	public static final String LIST_DOMAINS = "com.openshift.android.LIST_DOMAINS";
	public static final String USER_DETAIL = "com.openshift.android.USER_DETAIL";
	public static final String LIST_APPLICATIONS = "com.openshift.android.LIST_APPLICATIONS";
	public static final String APPLICATION_DETAIL_WITH_CARTRIDGE = "com.openshift.android.APPLICATION_DETAIL_WITH_CARTRIDGE";
	public static final String STOP_APPLICATION = "com.openshift.android.STOP_APPLICATION";
	public static final String START_APPLICATION = "com.openshift.android.START_APPLICATION";
	public static final String RESTART_APPLICATION = "com.openshift.android.RESTART_APPLICATION";
	public static final String DELETE_APPLICATION = "com.openshift.android.DELETE_APPLICATION";

}

