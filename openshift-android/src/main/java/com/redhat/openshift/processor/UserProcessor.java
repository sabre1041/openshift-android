package com.redhat.openshift.processor;

import java.lang.reflect.Type;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.redhat.openshift.ConfigurationManager;
import com.redhat.openshift.model.OpenshiftResponse;
import com.redhat.openshift.model.UserResource;
import com.redhat.openshift.rest.GsonSingleton;
import com.redhat.openshift.rest.OpenshiftRestClient;
import com.redhat.openshift.rest.Response;
import com.redhat.openshift.rest.RestMethod;
import com.redhat.openshift.rest.RestRequest;
import com.redhat.openshift.rest.RestResponse;
import com.redhat.openshift.service.OpenshiftCallback;

public class UserProcessor {

	private Context ctx;
	
	public UserProcessor(Context ctx) {
		this.ctx = ctx;
	}
	
	public void getUserInformation(OpenshiftCallback<UserResource> callback) {
		ConfigurationManager configuration = ConfigurationManager.getInstnace(ctx);
		
		RestRequest request = new RestRequest();
		request.setMethod(RestMethod.GET);
		request.setContextPath("user");
		
		OpenshiftRestClient client = new OpenshiftRestClient(configuration);
		RestResponse restResponse = client.execute(request);
		
		Response<UserResource> response = new Response<UserResource>();

		if(restResponse.getStatusCode()==200) {
			Gson gson = GsonSingleton.getInstance();
			Type type = new TypeToken<OpenshiftResponse<UserResource>>() {}.getType();
			
			OpenshiftResponse<UserResource> openshiftResponse = gson.fromJson(restResponse.getData(), type);
			response.setResource(openshiftResponse.getData());
		} 
		
		response.setStatus(restResponse.getStatusCode());
				
		response.setMessage(restResponse.getErrorMessage());
		
		callback.send(response);
		
	}
	
}
