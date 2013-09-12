package com.openshift.android.rest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.model.ApplicationAliasResource;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.EventType;
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
	
	public void getUser(Response.Listener<OpenshiftResponse<UserResource>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("user");
		
	    Type type = new TypeToken<OpenshiftResponse<UserResource>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}	
	
	public void listDomains(Response.Listener<OpenshiftResponse<OpenshiftDataList<DomainResource>>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<DomainResource>>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}
	
	public void listApplications(String domainName, Response.Listener<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}
	
	public void getApplicationWithCartridge(String domainName, String applicationName, Response.Listener<OpenshiftResponse<ApplicationResource>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		builder.appendPath(applicationName);
		
		builder.appendQueryParameter("include", "cartridges");
		
	    Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}
	
	public void getCartridge(String domainName, String applicationName, String cartridgeName, Response.Listener<OpenshiftResponse<CartridgeResource>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		builder.appendPath(applicationName);
		builder.appendPath("cartridges");
		builder.appendPath(cartridgeName);
		
		builder.appendQueryParameter("include", "status_messages");
		
	    Type type = new TypeToken<OpenshiftResponse<CartridgeResource>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}	
	

	public void getAvailableCartridges(Response.Listener<OpenshiftResponse<OpenshiftDataList<CartridgeResource>>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("cartridges");
				
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<CartridgeResource>>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}
	
	public void getApplicationAliases(String domainName, String applicationName, Response.Listener<OpenshiftResponse<OpenshiftDataList<ApplicationAliasResource>>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		builder.appendPath(applicationName);
		builder.appendPath("aliases");
				
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<ApplicationAliasResource>>>() {}.getType();

		processRequest(Method.GET, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}
	
	public void createApplicationAlias(ApplicationAliasResource alias, Response.Listener<OpenshiftResponse<ApplicationAliasResource>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(alias.getApplicationResource().getDomainId());
		builder.appendPath("applications");
		builder.appendPath(alias.getApplicationResource().getName());
		builder.appendPath("aliases");

		Map<String,String> params = new HashMap<String,String>();
		params.put("id", alias.getId());
		
	    Type type = new TypeToken<OpenshiftResponse<ApplicationAliasResource>>() {}.getType();

		processRequest(Method.POST, builder.build().toString(), type, null, params, listener, errorListener, tag);
		
	}

	
	
	/**
	 * Updates an Application Alias. NOTE: This method cannot be used until SSL Certificates are implemented into Application
	 * 
	 * @param alias
	 * @param listener
	 * @param errorListener
	 */
	public void updateApplicationAlias(ApplicationAliasResource alias, Response.Listener<OpenshiftResponse<ApplicationAliasResource>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(alias.getApplicationResource().getDomainId());
		builder.appendPath("applications");
		builder.appendPath(alias.getApplicationResource().getName());
		builder.appendPath("aliases");
				
	    Type type = new TypeToken<OpenshiftResponse<ApplicationAliasResource>>() {}.getType();

		processRequest(Method.PUT, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}
	
	public void removeAlias(String domainName, String applicationName, String aliasName, Response.Listener<OpenshiftResponse<ApplicationAliasResource>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		builder.appendPath(applicationName);
		builder.appendPath("aliases");
		builder.appendPath(aliasName);		
		
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<ApplicationAliasResource>>>() {}.getType();

		processRequest(Method.DELETE, builder.build().toString(), type, null, null, listener, errorListener, tag);
		
	}	
	
	public void createApplication(String domainName, String applicationName, String cartridgeName, Response.Listener<OpenshiftResponse<ApplicationResource>> listener, Response.ErrorListener errorListener, String tag) {

		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");

		Map<String,String> params = new HashMap<String,String>();
		params.put("name", applicationName);
		params.put("cartridge", cartridgeName);
		
	    Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();
		
		processRequest(Method.POST, builder.build().toString(), type, null, params, listener, errorListener, tag);
		
	}
	
	public void applicationEvent(String domainName, String applicationName, EventType eventType, Response.Listener<OpenshiftResponse<ApplicationResource>> listener, Response.ErrorListener errorListener, String tag) {
		
		Uri.Builder builder = getUriBuilder();
		builder.appendPath("domains");
		builder.appendPath(domainName);
		builder.appendPath("applications");
		builder.appendPath(applicationName);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("event", eventType.name().toLowerCase());
		
	    Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();
		
		if(eventType!=null) {
			
			switch(eventType) {
			case START:
			case STOP:
			case RESTART:
				builder.appendPath("events");
				processRequest(Method.POST, builder.build().toString(), type, null, params, listener, errorListener, tag);
				break;
			case DELETE:
				processRequest(Method.DELETE, builder.build().toString(), type, null, params, listener, errorListener, tag);
				break;
			}
			
		}
	}
	

	
	public <T> void processRequest(int method, String url, Type type, Map<String,String> headers, Map<String,String> params, Response.Listener<T> listener, Response.ErrorListener errorListener, String tag) {
		
		OpenshiftAndroidRequest<T> request = new OpenshiftAndroidRequest<T>(method, url, type, headers, params, listener, errorListener);
		
		//TODO: Need to determine how to handle timeout and retry policies
		request.setRetryPolicy(new DefaultRetryPolicy(
                120000, 
                0, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setTag(tag);
		
		if(Method.GET != request.getMethod()) {
			request.setShouldCache(false);
		}
		
		OpenshiftAndroidApplication.getInstance().getRequestQueue().getCache().invalidate(request.getCacheKey(), false);
		
		OpenshiftAndroidApplication.getInstance().getRequestQueue().add(request);
				
	}
	
	private Uri.Builder getUriBuilder() {
		
		Uri.Builder builder = Uri.parse(OpenshiftAndroidApplication.getInstance().getAuthorizationManger().getOpenshiftUrl()).buildUpon();
		builder.appendQueryParameter("nolinks", "true");
		
		
		return builder;
	}
	
	public void cancelRequests(String tag) {
		OpenshiftAndroidApplication.getInstance().getRequestQueue().cancelAll(tag);
	}
	
	

}
