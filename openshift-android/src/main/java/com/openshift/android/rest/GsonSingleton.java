package com.openshift.android.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openshift.android.marshall.OpenshiftDataListDeserializer;
import com.openshift.android.model.OpenshiftDataList;

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
