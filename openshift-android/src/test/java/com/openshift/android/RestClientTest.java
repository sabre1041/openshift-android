package com.openshift.android;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.GsonSingleton;
import com.openshift.android.rest.OpenshiftRestClient;
import com.openshift.android.rest.RestMethod;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.rest.RestResponse;

import junit.framework.TestCase;

public class RestClientTest extends TestCase {
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
//	public void testGet() {
//		ConfigurationManager configuration = ConfigurationManager.getTestInstnace();
//		configuration.setBaseUrl("");
//		configuration.setUserName("openshiftandroid@gmail.com");
//		configuration.setPassword("openshift2013");
//		
//		Type type = new TypeToken<OpenshiftResponse<DomainResource>>() {}.getType();
//		RestRequest<OpenshiftResponse<DomainResource>> domain = new RestRequest<OpenshiftResponse<DomainResource>>();
//		domain.setUrl("https://openshift.redhat.com/broker/rest/domain/openshiftandroid");
//		domain.setMethod(RestMethod.GET);
//		domain.setType(type);
//		
//		OpenshiftRestClient client = new OpenshiftRestClient(configuration);
//		RestResponse response = client.execute(domain);
//		System.out.println(response.getStatusCode());
//		System.out.println(response.getErrorMessage());
//		OpenshiftResponse<DomainResource> gsonResponse = transformJson(response.getData(), domain.getType());
//		System.out.println(gsonResponse.getMessages().get(0).getText());
//		
//	}
//	
//	
//	public void testPost() {
//		ConfigurationManager configuration = ConfigurationManager.getTestInstnace();
//		configuration.setBaseUrl("");
//		configuration.setUserName("openshiftandroid@gmail.com");
//		configuration.setPassword("openshift2013");
//		
//		Type type = new TypeToken<OpenshiftResponse<DomainResource>>() {}.getType();
//		RestRequest<OpenshiftResponse<DomainResource>> domain = new RestRequest<OpenshiftResponse<DomainResource>>();
//		domain.setUrl("https://openshift.redhat.com/broker/rest/domains/openshiftandroid/applications/eap/events");
//		domain.getInputParameters().put("event", "stop");
//		domain.setMethod(RestMethod.POST);
//		domain.setType(type);
//		
//		OpenshiftRestClient client = new OpenshiftRestClient(configuration);
//		RestResponse response = client.execute(domain);
//		System.out.println(response.getData());
//		OpenshiftResponse<DomainResource> gsonResponse = transformJson(response.getData(), domain.getType());
//		System.out.println(gsonResponse.getMessages().get(0).getText());
//	}
	
	public void testValid() {
		assert(true);
	}
	
	private <T> T transformJson(String json, Type type) {
		return GsonSingleton.getInstance().fromJson(json, type);
	}

}
