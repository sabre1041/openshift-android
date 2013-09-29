package com.openshift.android;

import static org.junit.Assert.*;

import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.openshift.android.marshall.OpenshiftDataListDeserializer;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.model.UserResource;

public class TestGsonSerialization   {
	
	private Gson gson;
	
	@Before
	public void setUp() throws Exception {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(OpenshiftDataList.class, new OpenshiftDataListDeserializer());
		gson = builder.create();
	}

	
	@Test
	public void testSerialization() {
		UserResource user = new UserResource();
		user.setLogin("openshiftandroid@gmail.com");
		
		String json = gson.toJson(user);
		assertNotNull(json);
		System.out.println(json);
	}
	
	@Test
	public void testDeserialization() {
		String json = "{\"api_version\":1.5,\"data\":{\"capabilities\":{\"subaccounts\":false,\"gear_sizes\":[\"small\"],\"plan_upgrade_enabled\":true},\"consumed_gears\":1,\"created_at\":\"2013-07-12T15:59:35Z\",\"id\":\"51e027e7e0b8cdeb3e0002b3\",\"login\":\"openshiftandroid@gmail.com\",\"max_gears\":3,\"plan_id\":\"free\",\"plan_state\":\"ACTIVE\",\"usage_account_id\":null},\"messages\":[],\"status\":\"ok\",\"supported_api_versions\":[1.0,1.1,1.2,1.3,1.4,1.5],\"type\":\"user\",\"version\":\"1.5\"}";
		
		Type type = new TypeToken<OpenshiftResponse<UserResource>>() {}.getType();
		
		OpenshiftResponse<UserResource> openshiftResponse = gson.fromJson(json, type);
		assertEquals("ok",openshiftResponse.getStatus());
		assertEquals("1.5",openshiftResponse.getApiVersion());
		assertNotNull(openshiftResponse);
	}
	
	@Test
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
	
	@Test
	public void testGetCartridgeList() {
		String json = "{   \"api_version\": 1.6,   \"data\": [     {       \"additional_gear_storage\": 0,       \"base_gear_storage\": 1,       \"collocated_with\": [         \"mysql-5.1\"       ],       \"current_scale\": 1,       \"description\": \"Market-leading open source enterprise platform for next-generation, highly transactional enterprise Java applications. Build and deploy enterprise Java in the cloud.\",       \"display_name\": \"JBoss Enterprise Application Platform 6.0\",       \"gear_profile\": \"small\",       \"help_topics\": [         {           \"Building with MySQL\": \"http://docs.redhat.com/docs/en-US/OpenShift/2.0/html/User_Guide/sect-User_Guide-Working_With_Database_Cartridges.html\"         }       ],       \"license\": \"LGPL\",       \"license_url\": \"http://www.gnu.org/copyleft/lesser.txt\",       \"name\": \"jbosseap-6.0\",       \"properties\": [         {           \"name\": \"OPENSHIFT_JBOSSEAP_LOG_DIR\",           \"type\": \"environment\",           \"description\": \"Directory to store application log files.\"         },         {           \"name\": \"OPENSHIFT_TMP_DIR\",           \"type\": \"environment\",           \"description\": \"Directory to store application temporary files.\"         },         {           \"name\": \"OPENSHIFT_REPO_DIR\",           \"type\": \"environment\",           \"description\": \"Application root directory where application files reside. This directory is reset every time you do a git-push\"         },         {           \"name\": \"OPENSHIFT_JBOSSEAP_HTTP_PORT\",           \"type\": \"environment\",           \"description\": \"Internal port to which the web-framework binds to.\"         },         {           \"name\": \"OPENSHIFT_JBOSSEAP_IP\",           \"type\": \"environment\",           \"description\": \"Internal IP to which the web-framework binds to.\"         },         {           \"name\": \"OPENSHIFT_APP_DNS\",           \"type\": \"environment\",           \"description\": \"Fully qualified domain name for the application.\"         },         {           \"name\": \"OPENSHIFT_APP_NAME\",           \"type\": \"environment\",           \"description\": \"Application name\"         },         {           \"name\": \"OPENSHIFT_DATA_DIR\",           \"type\": \"environment\",           \"description\": \"Directory to store application data files. Preserved across git-pushes. Not shared across gears.\"         },         {           \"name\": \"OPENSHIFT_APP_UUID\",           \"type\": \"environment\",           \"description\": \"Unique ID which identified the application. Does not change between gears.\"         },         {           \"name\": \"OPENSHIFT_GEAR_UUID\",           \"type\": \"environment\",           \"description\": \"Unique ID which identified the gear. This value changes between gears.\"         }       ],       \"scales_from\": 1,       \"scales_to\": 1,       \"scales_with\": null,       \"status_messages\": null,       \"supported_scales_from\": 1,       \"supported_scales_to\": 1,       \"tags\": [         \"service\",         \"web_framework\",         \"java\",         \"jboss\",         \"jee_full_profile\"       ],       \"type\": \"standalone\",       \"url\": null,       \"usage_rates\": [         {           \"plan_id\": \"silver\",           \"usd\": 0.03,           \"cad\": 0.03,           \"eur\": 0.02,           \"duration\": \"hour\"         }       ],       \"version\": \"6.0\",       \"website\": \"http://www.redhat.com/products/jbossenterprisemiddleware/application-platform/\"     },     {       \"additional_gear_storage\": 0,       \"base_gear_storage\": 1,       \"collocated_with\": [         \"jbosseap-6.0\"       ],       \"current_scale\": 1,       \"description\": \"MySQL is a multi-user, multi-threaded SQL database server.\",       \"display_name\": \"MySQL Database 5.1\",       \"gear_profile\": \"small\",       \"help_topics\": [         {           \"Building with MySQL\": \"http://docs.redhat.com/docs/en-US/OpenShift/2.0/html/User_Guide/sect-User_Guide-Working_With_Database_Cartridges.html\"         }       ],       \"license\": \"GPL\",       \"license_url\": \"\",       \"name\": \"mysql-5.1\",       \"properties\": [         {           \"name\": \"username\",           \"type\": \"cart_data\",           \"description\": \"Root user on mysql database\",           \"value\": \"adminYIAuKxk\"         },         {           \"name\": \"password\",           \"type\": \"cart_data\",           \"description\": \"Password for root user on mysql database\",           \"value\": \"GuRxkngNwUI5\"         },         {           \"name\": \"database_name\",           \"type\": \"cart_data\",           \"description\": \"MySQL DB name\",           \"value\": \"eap\"         },         {           \"name\": \"connection_url\",           \"type\": \"cart_data\",           \"description\": \"MySQL DB connection URL\",           \"value\": \"mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/\"         }       ],       \"scales_from\": 1,       \"scales_to\": 1,       \"scales_with\": null,       \"status_messages\": null,       \"supported_scales_from\": 1,       \"supported_scales_to\": 1,       \"tags\": [         \"service\",         \"database\",         \"embedded\"       ],       \"type\": \"embedded\",       \"url\": null,       \"usage_rates\": [],       \"version\": \"5.1\",       \"website\": \"http://www.mysql.com\"     }   ],   \"messages\": [     {       \"exit_code\": 0,       \"field\": null,       \"severity\": \"info\",       \"text\": \"Listing cartridges for application eap under domain openshiftandroid\"     }   ],   \"status\": \"ok\",   \"supported_api_versions\": [     1,     1.1,     1.2,     1.3,     1.4,     1.5,     1.6   ],   \"type\": \"cartridges\",   \"version\": \"1.6\" }";
		Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<CartridgeResource>>>() {}.getType();
		
		OpenshiftResponse<OpenshiftDataList<CartridgeResource>> openshiftResponse = gson.fromJson(json, type);
		CartridgeResource resource = openshiftResponse.getData().getList().get(0);
		assertEquals("0",resource.getAdditionalGearStorage());
		assertEquals(1,resource.getCollocatedWith().size());
		assertEquals("mysql-5.1",resource.getCollocatedWith().get(0));
		assertEquals("service", resource.getTags().get(0));
	}
}
