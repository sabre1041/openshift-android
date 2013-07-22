package com.redhat.openshift.processor;

import android.content.Context;

import com.google.gson.Gson;
import com.redhat.openshift.model.OpenshiftResource;
import com.redhat.openshift.model.OpenshiftResponse;
import com.redhat.openshift.rest.GsonSingleton;
import com.redhat.openshift.rest.OpenshiftRestClient;
import com.redhat.openshift.rest.RestRequest;
import com.redhat.openshift.rest.RestResponse;
import com.redhat.openshift.security.AuthorizationManager;
import com.redhat.openshift.service.OpenshiftCallback;

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
