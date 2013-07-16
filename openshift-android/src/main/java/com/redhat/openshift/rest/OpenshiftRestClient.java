package com.redhat.openshift.rest;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.redhat.openshift.ConfigurationManager;

public class OpenshiftRestClient {
	
	private DefaultHttpClient  client;
	private ConfigurationManager configuration;
	
	public OpenshiftRestClient(ConfigurationManager configuration) {
		
		this.configuration = configuration;
		
		client = new DefaultHttpClient();
		
		// Add Authentication if Exists
		if(configuration.getUserName()!=null && configuration.getPassword()!=null) {
			Credentials credentials = new UsernamePasswordCredentials(configuration.getUserName(), configuration.getPassword());
			client.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
		}
		
	}
	
	
	public RestResponse execute(RestRequest request) {
		
		switch(request.getMethod()) {
		case GET:
			return get(request);
		default:
			return new RestResponse(-1,"Unknown Rest Method", null);
		
		}
		
	}
	
	public RestResponse get(RestRequest request) {
		String url = configuration.getBaseUrl()+"/"+request.getContextPath();
		
		HttpGet httpRequest = new HttpGet(url);
		httpRequest.setHeader("Accept","application/json");

		RestResponse restResponse = new RestResponse();
		
		try {
			HttpResponse response = client.execute(httpRequest);
			if(response.getEntity()!= null) {
				String entityString = EntityUtils.toString(response.getEntity());
				restResponse.setData(entityString);
			}
			
			restResponse.setStatusCode(response.getStatusLine().getStatusCode());
			restResponse.setErrorMessage(response.getStatusLine().getReasonPhrase());
			
		} catch (Exception e) {
			restResponse.setStatusCode(e.hashCode());
			restResponse.setErrorMessage("Error Message: "+e);
		}
		
		return restResponse;
	}
	

}
