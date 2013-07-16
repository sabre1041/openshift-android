package com.redhat.openshift.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redhat.openshift.marshall.OpenshiftDataListDeserializer;
import com.redhat.openshift.model.OpenshiftDataList;

public class GsonSingleton {
	
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(OpenshiftDataList.class, new OpenshiftDataListDeserializer());
		gson = builder.create();
	}
	
	private static final Gson gson;
	
	private GsonSingleton() {}
	
	public static Gson getInstance() {
		return gson;
	}

}
