package com.openshift.android.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Contains the logic for storing and retrieving the credentials for a user 
 * 
 * @author Andrew Block
 * @see SharedPreferences
 *
 */
/**
 * @author Andrew Block
 *
 */
/**
 * @author Andrew Block
 *
 */
public class AuthorizationManager {
	
	private final SharedPreferences sharedPreferences;
	
	private static AuthorizationManager instance;
	
	private static final String PREF_NAME_OPENSHIFT_ACCOUNT = "openshiftAccount";
	private static final String PREF_NAME_OPENSHIFT_PASSWORD = "openshiftPassword";
	private static final String PREF_NAME_OPENSHIFT_URL = "openshiftUrl";

	
	
	private AuthorizationManager(Context ctx) {		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
		
	}
	
	/**
	 * Method to allow for the creation of an instance of this class
	 * 
	 * @param ctx Android Context
	 * @return An Instance of this class
	 */
	public static AuthorizationManager getInstance(Context ctx) {
		if(instance == null) {
			instance = new AuthorizationManager(ctx);
		}
		return instance;
	}
	
	/**
	 * @param openshiftAccount The Openshift User Account
	 * @param openshiftPassword The Openshift Password
	 * @param openshiftUrl The Base URL for OpenShift Rest Requests
	 */
	public void saveAuthentication(String openshiftAccount, String openshiftPassword, String openshiftUrl) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(PREF_NAME_OPENSHIFT_ACCOUNT, openshiftAccount);
		editor.putString(PREF_NAME_OPENSHIFT_PASSWORD, openshiftPassword);
		editor.putString(PREF_NAME_OPENSHIFT_URL, openshiftUrl);
		editor.commit();
		
	} 	
	
	/**
	 * Check to see if a user is currently autenticated
	 * 
	 * @return boolean indicating whether a user is currently authenticated
	 */
	public boolean checkAuthentication() {
		String openshiftAccount = sharedPreferences.getString(PREF_NAME_OPENSHIFT_ACCOUNT, null);
		String openshiftPassword = sharedPreferences.getString(PREF_NAME_OPENSHIFT_PASSWORD, null);
		String openshiftUrl = sharedPreferences.getString(PREF_NAME_OPENSHIFT_URL, null);
		
		if(openshiftAccount == null || openshiftPassword == null || openshiftUrl == null) return false;
		
		return true;
		
	}
	
	
	/**
	 * Invalidate the current user session by removing the contents of the {@link SharedPreferences}
	 */
	public void invalidateAuthentication() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	
	/**
	 * @return the Openshift Account
	 */
	public String getOpenshiftAccount() {
		return sharedPreferences.getString(PREF_NAME_OPENSHIFT_ACCOUNT, null);
	}
	
	/**
	 * @return The Openshift Password
	 */
	public String getOpenshiftPassword() {
		return sharedPreferences.getString(PREF_NAME_OPENSHIFT_PASSWORD, null);
	}
	
	/**
	 * @return The Openshift Rest Service base URL
	 */
	public String getOpenshiftUrl() {
		return sharedPreferences.getString(PREF_NAME_OPENSHIFT_URL, null);
	}

}
