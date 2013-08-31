package com.openshift.android.rest;

import java.lang.reflect.Type;
import java.util.Map;

import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.google.gson.reflect.TypeToken;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.model.UserResource;

public class OpenshiftRestManager {
	
	private static OpenshiftRestManager instance;
	
	private OpenshiftRestManager() {
	}
	
	public static OpenshiftRestManager getInstance() {
		if(instance == null) {
			instance = new OpenshiftRestManager();
		}
		
		return instance;
	}
	
	public void getUser(com.android.volley.Response.Listener<OpenshiftResponse<UserResource>> listener, com.android.volley.Response.ErrorListener errorListener) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("user");
		
	    Type type = new TypeToken<OpenshiftResponse<UserResource>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener);
		
	}	
	
	public void listDomains(com.android.volley.Response.Listener<OpenshiftResponse<OpenshiftDataList<DomainResource>>> listener, com.android.volley.Response.ErrorListener errorListener) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<DomainResource>>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener);
		
	}
	
	public void listApplications(String domainName, com.android.volley.Response.Listener<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>> listener, com.android.volley.Response.ErrorListener errorListener) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener);
		
	}
	
	public void getApplicationWithCartridge(String domainName, String applicationName, com.android.volley.Response.Listener<OpenshiftResponse<ApplicationResource>> listener, com.android.volley.Response.ErrorListener errorListener) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		builder.appendPath(applicationName);
		
		builder.appendQueryParameter("include", "cartridges");
		
	    Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener);
		
	}
	
	public void getCartridge(String domainName, String applicationName, String cartridgeName, com.android.volley.Response.Listener<OpenshiftResponse<CartridgeResource>> listener, com.android.volley.Response.ErrorListener errorListener) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		builder.appendPath(applicationName);
		builder.appendPath("cartridges");
		builder.appendPath(cartridgeName);
		
		builder.appendQueryParameter("include", "status_messages");
		
	    Type type = new TypeToken<OpenshiftResponse<CartridgeResource>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener);
		
	}	
	

	
	public <T> void processRequest(int method, String url, Type type, Map<String,String> headers, Map<String,String> params, com.android.volley.Response.Listener<T> listener, com.android.volley.Response.ErrorListener errorListener) {
		
		OpenshiftAndroidRequest<T> request = new OpenshiftAndroidRequest<T>(method, url, type, headers, params, listener, errorListener);
		
		request.setRetryPolicy(new DefaultRetryPolicy(
                50000, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		
		OpenshiftAndroidApplication.getInstance().getRequestQueue().add(request);
		
	}
	
	private Uri.Builder getUriBuilder() {
		
		Uri.Builder builder = Uri.parse(OpenshiftAndroidApplication.getInstance().getAuthorizationManger().getOpenshiftUrl()).buildUpon();
		builder.appendQueryParameter("nolinks", "true");
		
		
		return builder;
	}
	
	

}
