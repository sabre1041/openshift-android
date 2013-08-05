package com.openshift.android;

import java.lang.reflect.Type;

import junit.framework.TestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.openshift.android.marshall.OpenshiftDataListDeserializer;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.model.UserResource;

public class TestGsonSerialization extends TestCase  {
	
	private Gson gson;
	
	public void setUp() throws Exception {
		super.setUp();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(OpenshiftDataList.class, new OpenshiftDataListDeserializer());
		gson = builder.create();
	}

	
	public void testSerialization() {
		UserResource user = new UserResource();
		user.setLogin("openshiftandroid@gmail.com");
		
		String json = gson.toJson(user);
		assertNotNull(json);
		System.out.println(json);
	}
	
	public void testDeserialization() {
		String json = "{\"api_version\":1.5,\"data\":{\"capabilities\":{\"subaccounts\":false,\"gear_sizes\":[\"small\"],\"plan_upgrade_enabled\":true},\"consumed_gears\":1,\"created_at\":\"2013-07-12T15:59:35Z\",\"id\":\"51e027e7e0b8cdeb3e0002b3\",\"login\":\"openshiftandroid@gmail.com\",\"max_gears\":3,\"plan_id\":\"free\",\"plan_state\":\"ACTIVE\",\"usage_account_id\":null},\"messages\":[],\"status\":\"ok\",\"supported_api_versions\":[1.0,1.1,1.2,1.3,1.4,1.5],\"type\":\"user\",\"version\":\"1.5\"}";
		
		Type type = new TypeToken<OpenshiftResponse<UserResource>>() {}.getType();
		
		OpenshiftResponse<UserResource> openshiftResponse = gson.fromJson(json, type);
		assertEquals("ok",openshiftResponse.getStatus());
		assertEquals("1.5",openshiftResponse.getApiVersion());
		assertNotNull(openshiftResponse);
	}
	
	public void testGetDomains() {
		String json = "{\"api_version\":1.5,\"data\":[{\"id\":\"openshiftandroid\",\"suffix\":\"rhcloud.com\"}],\"messages\":[],\"status\":\"ok\",\"supported_api_versions\":[1.0,1.1,1.2,1.3,1.4,1.5],\"type\":\"domains\",\"version\":\"1.5\"}";
		Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<DomainResource>>>() {}.getType();
		
		OpenshiftResponse<OpenshiftDataList<DomainResource>> openshiftResponse = gson.fromJson(json, type);
		assertEquals("ok",openshiftResponse.getStatus());
		assertEquals("1.5",openshiftResponse.getApiVersion());
		assertNotNull(openshiftResponse);
		assertNotNull(openshiftResponse.getData());
		assertEquals(1,openshiftResponse.getData().getList().size());

		
	}
}
