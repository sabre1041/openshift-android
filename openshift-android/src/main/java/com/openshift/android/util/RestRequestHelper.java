package com.openshift.android.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.rest.RestRequest;

public class RestRequestHelper {
	
	private static final String SEPERATOR = "_";
	private static final String CACHE_CATEGORY = "REST";
	
	public static String getCacheKey(RestRequest<? extends OpenshiftResource> restRequest) {
		
		if(restRequest.getMethod() == null ||restRequest.getUrl() == null || restRequest.getInputParameters() == null) {
			throw new IllegalArgumentException("Cannot Produce Cache Key. Required Parameters Not Set");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(CACHE_CATEGORY);
		sb.append(SEPERATOR);
		sb.append(restRequest.getMethod().name());
		sb.append(SEPERATOR);
		sb.append(getEncodedUrl(restRequest));
		
		return sb.toString();
	}
	
	public static String getEncodedUrl(RestRequest<? extends OpenshiftResource> restRequest) {
		
		if(restRequest.getUrl() == null || restRequest.getInputParameters() == null) {
			throw new IllegalArgumentException("Cannot Produce Encoded URL. Required Parameters Not Set");
		}
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		
		for(String key : restRequest.getInputParameters().keySet()) {
			queryParams.add(new BasicNameValuePair(key, restRequest.getInputParameters().get(key)));
		}
				
		return restRequest.getInputParameters().size() == 0 ? restRequest.getUrl() : restRequest.getUrl()+"?"+URLEncodedUtils.format(queryParams, "utf-8");
		
	}
	
	

}
