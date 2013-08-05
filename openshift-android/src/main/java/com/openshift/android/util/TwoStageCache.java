package com.openshift.android.util;

import android.content.Context;

/**
 * Class implementing a two stage caching mechanism
 *
 * <p>The first stage is an in-memory LRU cache.  This cache is
 * used to provide quick access to recently used data to improve
 * response times.
 *
 * <p>The second stage is a filesystem or database backed 
 * persistence store. This stage provides a larger storage
 * space and persistence between starting and stopping the
 * application.
 *
 * <p>NOTE: A Filesystem persistence store is used by default
 *
 * @author Joey Yore
 * @version 1.0
 */
public class TwoStageCache {

	/** Option for using a filesystem backed persistence store */
	public static final int FILESYSTEM_CACHE = 0;

	/** Option for using a database backed persistence store */
	public static final int DATABASE_CACHE = 1;

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
	
	/**
	 * Simple constructor
	 *
	 * @param context The android context to use
	 */
	public TwoStageCache(Context context) {
		this.context = context;
		memCache = new LRUCache<String, Object>();
		diskCache = new FileSystemStore(context);
	}
	
	/**
	 * Persistence Select Constructor
	 *
	 * Allows you to select which type of persistence store to
	 * use for the second stage cache.
	 *
	 * @param context The android context to use
	 * @param dataStoreOption The datastore type option to use.
	 */
	public TwoStageCache(Context context, int dataStoreOption) {
		this.context = context;
		this.dataStoreOption = dataStoreOption;
		memCache = new LRUCache<String,Object>();
		diskCache = selectDataStore(context,"",dataStoreOption);
	}
	
	/**
	 * LRU Config Constructor
	 *
	 * This constructor allows you to select the persistence cache type,
	 * as well as provide configuration points for the in-memory cache.
	 *
	 * @param context The android context to use
	 * @param dataStoreOption The datastore type option to use.
	 * @param age The time (in milliseconds) that an entry can exist in-memory
	 * @param size The maximum number of entries in the in-memory cache.
	 */
	public TwoStageCache(Context context, int dataStoreOption, long age, int size) {
		this.context = context;
		this.dataStoreOption = dataStoreOption;
		memCache = new LRUCache<String,Object>(age,size);
		diskCache = selectDataStore(context,"",dataStoreOption);
	}
	

	/**
	 * Namespace Constructor
	 *
	 * This constructor allows configuration of the in-memory cache,
	 * the ability to select a persistence store type, and provide
	 * a namespace so multiple two-stage caches can exist in an application
	 * without collision.
	 *
	 * @param context The android context to use
	 * @param dataStoreOption The datastore type option to use.
	 * @param age The time (in milliseconds) that an entry can exist in-memory
	 * @param size The maximum number of entries in the in-memory cache.
	 * @param contextRoot The context-root the namespace lives under
	 */

	public TwoStageCache(Context context, int dataStoreOption, long age, int size, String contextRoot) {
		this.context = context;
		this.dataStoreOption = dataStoreOption;
		memCache = new LRUCache<String,Object>(age,size);
		diskCache = selectDataStore(context,contextRoot,dataStoreOption);
	}
	
	/**
	 * Retrieve an object from the cache
	 *
	 * <p>This method searches the in-memory cache for the entry first.
	 * If the get method is a miss, then it will search the datastore 
	 * for the entry.
	 *
	 * <p>If retrieval from either cache is a success, then the object is
	 * returned. If both caches miss, then a null object is returned
	 * 
	 * @param key The key to search for
	 * @return The retrieved object or null
	 */
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
	
	/**
	 * Add an object to the caches
	 *
	 * This method will insert or update an entry in the cache
	 *
	 * @param key The key to insert/update
	 * @param value The object to store
	 */
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
