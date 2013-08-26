package com.openshift.android.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.openshift.android.OpenshiftConstants;
import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.security.AuthorizationManager;
import com.openshift.android.util.RestRequestHelper;

/**
 * Rest Client for the Application
 * 
 * @author Andrew Block
 *
 */
public class OpenshiftRestClient {
	
	private DefaultHttpClient  client;
	private AuthorizationManager authorizationManager;
	
	/**
	 * Default constructor which takes an instance of an {@link AuthorizationManager} which contains the
	 * user name and password used to authenticate with OpenShift
	 * 
	 * @param authorizationManager
	 */
	public OpenshiftRestClient(AuthorizationManager authorizationManager) {
		
		this.authorizationManager = authorizationManager;
		
		client = new DefaultHttpClient();
		
		// Add Authentication if Exists
		if(authorizationManager.getOpenshiftAccount()!=null && authorizationManager.getOpenshiftPassword()!=null) {
			Credentials credentials = new UsernamePasswordCredentials(authorizationManager.getOpenshiftAccount(), authorizationManager.getOpenshiftPassword());
			client.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
		}
		
	}
	
	
	/**
	 * Performs the execution of the Rest Request
	 * 
	 * @param The request parameters for the Rest Client
	 * @return The response from the Rest Client
	 */
	public RestResponse execute(RestRequest<? extends OpenshiftResource> request) {
		
		switch(request.getMethod()) {
		case GET:
			return get(request);
		case POST:
			return post(request);
		case DELETE:
			return delete(request);
		default:
			return new RestResponse(-1,"Unknown Rest Method", null);
		
		}
		
	}
	
	/**
	 * Performs the GET operation of the Rest Client
	 * 
	 * @param The request parameters for the Rest Client
	 * @return The response from the Rest Client
	 */
	public RestResponse get(RestRequest<? extends OpenshiftResource> request) {
		
		request.getInputParameters().put("nolinks", "true");
		
		String encodedUrl = RestRequestHelper.getEncodedUrl(request);
		
		HttpGet httpRequest = new HttpGet(encodedUrl);
		httpRequest.setHeader("Accept","application/json; version="+OpenshiftConstants.REST_API_VERSION);

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
			restResponse.setStatusCode(0);
			restResponse.setErrorMessage("Error Message: "+e);
		}
		
		return restResponse;
	}
	
	
	/**
	 * Performs the DELETE operation of the Rest Client
	 * 
	 * @param The request parameters for the Rest Client
	 * @return The response from the Rest Client
	 */
	public RestResponse delete(RestRequest<? extends OpenshiftResource> request) {
		
		HttpDelete httpRequest = new HttpDelete(request.getUrl());
		httpRequest.setHeader("Accept","application/json; version="+OpenshiftConstants.REST_API_VERSION);

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
			restResponse.setStatusCode(0);
			restResponse.setErrorMessage("Error Message: "+e);
		}
		
		return restResponse;
	}


	/**
	 * Performs the POST operation of the Rest Client
	 * 
	 * @param The request parameters for the Rest Client
	 * @return The response from the Rest Client
	 */
	public RestResponse post(RestRequest<? extends OpenshiftResource> request) {
		
		HttpPost httpRequest = new HttpPost(request.getUrl());
		httpRequest.setHeader("Accept","application/json; version="+OpenshiftConstants.REST_API_VERSION);

		RestResponse restResponse = new RestResponse();
		
		try {
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		      if(request.getInputParameters()!=null) {
		    	  for(String key : request.getInputParameters().keySet()) {
		    		  nameValuePairs.add(new BasicNameValuePair(key, request.getInputParameters().get(key)));
		    	  }
		      }
		    
		      
		    httpRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
