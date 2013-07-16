package com.redhat.openshift;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;

public class ConfigurationManager {
	
	private String baseUrl;
	private String userName;
	private String password;
	
	private static ConfigurationManager instance;
	
	private static final String CONFIGURATION_FILE = "openshiftconfiguration.properties";
	private static final String BASE_URL_PROP = "openshiftconfiguration.baseurl";
	private static final String USERNAME_PROP = "openshiftconfiguration.username";
	private static final String PASSWORD_PROP = "openshiftconfiguration.password";
	
	private ConfigurationManager(Context context) {
		AssetManager assetManager = context.getAssets();
		
		try {
			InputStream is = assetManager.open(CONFIGURATION_FILE);
			
			Properties props = new Properties();
			props.load(is);

			
			userName = props.getProperty(USERNAME_PROP);
			password = props.getProperty(PASSWORD_PROP);
			baseUrl = props.getProperty(BASE_URL_PROP);
			
		} catch(Exception e) {
			System.out.println("Unable to load properties!");
			e.printStackTrace();
		}
		
	}
	
	public static ConfigurationManager getInstnace(Context ctx) {
		if(instance == null) {
			instance = new ConfigurationManager(ctx);
		}
		
		return instance;
	}
	
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
