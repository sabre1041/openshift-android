package com.openshift.android.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AuthorizationManager {
	
	private final SharedPreferences sharedPreferences;
	
	private static AuthorizationManager instance;
	
	private static final String PREF_NAME_OPENSHIFT_ACCOUNT = "openshiftAccount";
	private static final String PREF_NAME_OPENSHIFT_PASSWORD = "openshiftPassword";
	private static final String PREF_NAME_OPENSHIFT_URL = "openshiftUrl";

	
	private AuthorizationManager(Context ctx) {		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
		
	}
	
	public static AuthorizationManager getInstance(Context ctx) {
		if(instance == null) {
			instance = new AuthorizationManager(ctx);
		}
		return instance;
	}
	
	public void saveAuthentication(String openshiftAccount, String openshiftPassword, String openshiftUrl) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(PREF_NAME_OPENSHIFT_ACCOUNT, openshiftAccount);
		editor.putString(PREF_NAME_OPENSHIFT_PASSWORD, openshiftPassword);
		editor.putString(PREF_NAME_OPENSHIFT_URL, openshiftUrl);
		editor.commit();
		
	} 	
	
	public boolean checkAuthentication() {
		String openshiftAccount = sharedPreferences.getString(PREF_NAME_OPENSHIFT_ACCOUNT, null);
		String openshiftPassword = sharedPreferences.getString(PREF_NAME_OPENSHIFT_PASSWORD, null);
		String openshiftUrl = sharedPreferences.getString(PREF_NAME_OPENSHIFT_URL, null);
		
		if(openshiftAccount == null || openshiftPassword == null || openshiftUrl == null) return false;
		
		return true;
		
	}
	
	public void invalidateAuthentication() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	public String getOpenshiftAccount() {
		return sharedPreferences.getString(PREF_NAME_OPENSHIFT_ACCOUNT, null);
	}
	
	public String getOpenshiftPassword() {
		return sharedPreferences.getString(PREF_NAME_OPENSHIFT_PASSWORD, null);
	}
	
	public String getOpenshiftUrl() {
		return sharedPreferences.getString(PREF_NAME_OPENSHIFT_URL, null);
	}

}
