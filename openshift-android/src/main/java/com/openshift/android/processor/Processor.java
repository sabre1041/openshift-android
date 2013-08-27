package com.openshift.android.processor;

import android.content.Context;

import com.google.gson.Gson;
import com.openshift.android.cache.TwoStageCache;
import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.GsonSingleton;
import com.openshift.android.rest.OpenshiftRestClient;
import com.openshift.android.rest.RestMethod;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.rest.RestResponse;
import com.openshift.android.security.AuthorizationManager;
import com.openshift.android.service.OpenshiftCallback;
import com.openshift.android.util.RestRequestHelper;


/**
 * Process the rest Request
 * 
 * @author Andrew Block
 *
 */
public class Processor {
	
	private OpenshiftCallback callback;
	
	private Context ctx;
	
	/**
	 * @param ctx The Android Context
	 */
	public Processor(Context ctx) {
		this.ctx = ctx;
	}
	
	
	
	/**
	 * @param callback The callback for the Rest Client to invoke after it has finished executing
	 */
	public void setCallback(OpenshiftCallback callback) {
		this.callback = callback;
	}
	
	/**
	 * Performs execution of the Rest Requst by invoking the Rest Client
	 * 
	 * @param request the Rest Request
	 * 
	 */
	public void execute(RestRequest<? extends OpenshiftResource> request) {
		AuthorizationManager authorizationManager = AuthorizationManager.getInstance(ctx);
		
		OpenshiftRestClient client = new OpenshiftRestClient(authorizationManager);

		RestResponse restResponse = client.execute(request);
		
		if(restResponse.getStatusCode()==200 && request.getType()!= null) {
			Gson gson = GsonSingleton.getInstance();
			
			OpenshiftResponse<? extends OpenshiftResource> openshiftResponse = gson.fromJson(restResponse.getData(), request.getType());
			request.setResponse(openshiftResponse);
			
			if(RestMethod.GET.equals(request.getMethod())) {
				// Put in Cache
				TwoStageCache.getInstance(ctx).put(RestRequestHelper.getCacheKey(request), openshiftResponse);	
			}
		}
		
		request.setStatus(restResponse.getStatusCode());

		callback.send(request.getStatus(),request);
	}

	
	
	

}
