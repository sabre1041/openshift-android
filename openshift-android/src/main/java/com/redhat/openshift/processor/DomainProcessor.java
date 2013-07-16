package com.redhat.openshift.processor;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.redhat.openshift.ConfigurationManager;
import com.redhat.openshift.model.DomainResource;
import com.redhat.openshift.model.OpenshiftDataList;
import com.redhat.openshift.model.OpenshiftResponse;
import com.redhat.openshift.rest.GsonSingleton;
import com.redhat.openshift.rest.OpenshiftRestClient;
import com.redhat.openshift.rest.Response;
import com.redhat.openshift.rest.RestMethod;
import com.redhat.openshift.rest.RestRequest;
import com.redhat.openshift.rest.RestResponse;
import com.redhat.openshift.service.OpenshiftCallback;

public class DomainProcessor {
	
	private Context ctx;
	
	public DomainProcessor(Context ctx) {
		this.ctx = ctx;
	}
	
	public void getDomains(OpenshiftCallback<OpenshiftDataList<DomainResource>> callback) {
		ConfigurationManager configuration = ConfigurationManager.getInstnace(ctx);
		
		RestRequest request = new RestRequest();
		request.setMethod(RestMethod.GET);
		request.setContextPath("domains");
		
		OpenshiftRestClient client = new OpenshiftRestClient(configuration);
		RestResponse restResponse = client.execute(request);
		
		Response<OpenshiftDataList<DomainResource>> response = new Response<OpenshiftDataList<DomainResource>>();

		if(restResponse.getStatusCode()==200) {
			Gson gson = GsonSingleton.getInstance();
			Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<DomainResource>>>() {}.getType();
			
			OpenshiftResponse<OpenshiftDataList<DomainResource>> openshiftResponse = gson.fromJson(restResponse.getData(), type);
			response.setResource(openshiftResponse.getData());
		} 
		
		response.setStatus(restResponse.getStatusCode());
				
		response.setMessage(restResponse.getErrorMessage());
		
		callback.send(response);
	}

}
