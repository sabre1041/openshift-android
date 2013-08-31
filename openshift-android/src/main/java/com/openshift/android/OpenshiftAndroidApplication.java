package com.openshift.android;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.openshift.android.security.AuthorizationManager;

public class OpenshiftAndroidApplication extends Application {
	
	private RequestQueue requestQueue;
	private AuthorizationManager authorizationManager;
	private static OpenshiftAndroidApplication openshiftAndroidApplication;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		openshiftAndroidApplication = this;
		requestQueue = Volley.newRequestQueue(this);
		authorizationManager = AuthorizationManager.getInstance(this);
	}
	
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}
	
	public AuthorizationManager getAuthorizationManger() {
		return authorizationManager;
	}
	
	public static OpenshiftAndroidApplication getInstance() {
		return openshiftAndroidApplication;
	}
	

}
