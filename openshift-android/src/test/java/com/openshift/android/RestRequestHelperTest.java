package com.openshift.android;

import junit.framework.TestCase;

import com.openshift.android.rest.RestMethod;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.util.RestRequestHelper;

public class RestRequestHelperTest extends TestCase  {
	
	private static final String BASE_URL = "https://openshift.com/";
	
	public void setUp() throws Exception {
		super.setUp();
	}

	
	public void testSimpleCache() {
		RestRequest restRequest = new RestRequest();
		restRequest.setMethod(RestMethod.GET);
		restRequest.setUrl(BASE_URL+"domains");
		System.out.println(RestRequestHelper.getCacheKey(restRequest));
	}
	
	public void testFormatUrl() {
		RestRequest restRequest = new RestRequest();
		restRequest.setMethod(RestMethod.GET);
		restRequest.setUrl(BASE_URL+"domains");
		System.out.println(RestRequestHelper.getCacheKey(restRequest));
		restRequest.getInputParameters().put("key", "value");
		System.out.println(RestRequestHelper.getEncodedUrl(restRequest));
	}

}
