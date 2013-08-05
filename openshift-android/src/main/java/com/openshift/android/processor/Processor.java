package com.openshift.android.processor;

import android.content.Context;

import com.google.gson.Gson;
import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.GsonSingleton;
import com.openshift.android.rest.OpenshiftRestClient;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.rest.RestResponse;
import com.openshift.android.security.AuthorizationManager;
import com.openshift.android.service.OpenshiftCallback;

public class Processor {
	
	private OpenshiftCallback callback;
	
	private Context ctx;
	
	public Processor(Context ctx) {
		this.ctx = ctx;
	}
	
	
	
	public void setCallback(OpenshiftCallback callback) {
		this.callback = callback;
	}
	
	public void execute(RestRequest<? extends OpenshiftResource> request) {
		AuthorizationManager authorizationManager = AuthorizationManager.getInstance(ctx);
		
		OpenshiftRestClient client = new OpenshiftRestClient(authorizationManager);

		RestResponse restResponse = client.execute(request);
		
		if(restResponse.getStatusCode()==200 && request.getType()!= null) {
			Gson gson = GsonSingleton.getInstance();
			
			OpenshiftResponse<? extends OpenshiftResource> openshiftResponse = gson.fromJson(restResponse.getData(), request.getType());
			request.setResponse(openshiftResponse);
		}
		
		request.setStatus(restResponse.getStatusCode());

		callback.send(request.getStatus(),request);
	}

	
	
	

}
