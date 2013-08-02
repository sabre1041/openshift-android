package com.redhat.openshift.util;

import android.content.Context;


public class TwoStageCache {

	private static final int FILESYSTEM_CACHE = 0;
	private static final int DATABASE_CACHE = 1;
	
	@SuppressWarnings("unused")
	private Context context;
	private int dataStoreOption;
	
	private static LRUCache<String,Object> memCache;
	private DataStore diskCache;
	
	
	private DataStore selectDataStore(Context context, String contextRoot, int option) {
		switch(option) {
			case FILESYSTEM_CACHE:
				return new FileSystemStore(context,contextRoot);
			case DATABASE_CACHE:
				return new DatabaseStore(context,contextRoot);
			default:
				return new FileSystemStore(context,contextRoot);
		}
	}
	
	public TwoStageCache(Context context) {
		this.context = context;
		memCache = new LRUCache<String, Object>();
		diskCache = new FileSystemStore(context);
	}
	
	public TwoStageCache(Context context, int dataStoreOption) {
		this.context = context;
		this.dataStoreOption = dataStoreOption;
		memCache = new LRUCache<String,Object>();
		diskCache = selectDataStore(context,"",dataStoreOption);
	}
	
	public TwoStageCache(Context context, int dataStoreOption, long age, int size) {
		this.context = context;
		this.dataStoreOption = dataStoreOption;
		memCache = new LRUCache<String,Object>(age,size);
		diskCache = selectDataStore(context,"",dataStoreOption);
	}
	
	public TwoStageCache(Context context, int dataStoreOption, long age, int size, String contextRoot) {
		this.context = context;
		this.dataStoreOption = dataStoreOption;
		memCache = new LRUCache<String,Object>(age,size);
		diskCache = selectDataStore(context,contextRoot,dataStoreOption);
	}
	
	public Object get(String key) {
		
		Object o = memCache.get(key);
		if(o == null) {
			o = diskCache.get(key);
			if(o != null) {
				memCache.put(key, o);
			}
		}
		return o;
	}
	
	public void put(String key,Object value) {
		
		memCache.put(key, value);	
		if(dataStoreOption == DATABASE_CACHE) {
			Object o = diskCache.get(key);
			if(o == null) {
				diskCache.create(key, value);
			} else {
				diskCache.update(key, value);
			}
		} else {
			diskCache.create(key, value);
		}
	}
}