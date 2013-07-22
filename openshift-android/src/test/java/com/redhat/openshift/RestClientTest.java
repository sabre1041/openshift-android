package com.redhat.openshift;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.redhat.openshift.model.DomainResource;
import com.redhat.openshift.model.OpenshiftDataList;
import com.redhat.openshift.model.OpenshiftResponse;
import com.redhat.openshift.rest.GsonSingleton;
import com.redhat.openshift.rest.OpenshiftRestClient;
import com.redhat.openshift.rest.RestMethod;
import com.redhat.openshift.rest.RestRequest;
import com.redhat.openshift.rest.RestResponse;

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
