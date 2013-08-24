package com.openshift.android.cache;

import android.content.Context;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;

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


	private static TwoStageCache instance = null;
	@SuppressWarnings("unused")
	private static Context context;
	private static int dataStoreOption;
	
	private static LRUCache<String,Object> memCache;
	private static DataStore diskCache;
	
	public TwoStageCache() {
	}


	/**
	 * Get Singleton Instance
	 *
	 * @throws IllegalArgumentException Throws if the instance has not been initialized with an Android context.
	 */
	public static TwoStageCache getInstance() throws IllegalArgumentException {
		if(instance == null) {
			throw new IllegalArgumentException("Cache has not been initialized with an Android context");
		}
		return instance;
	}

    /**
	 * Simple Instance Initialization
	 *
	 * @param context The android context to use
	 * @return The Singleton Instance
	 */
	public static TwoStageCache getInstance(Context context) {
		if(instance == null) {
			instance = new TwoStageCache();
			instance.context = context;
			memCache = new LRUCache<String, Object>();
			diskCache = new FileSystemStore(context);
		}
		return instance;
	}

	/**
	 * Persistence Instance Initialization
	 *
	 * Allows you to select which type of persistence store to
	 * use for the second stage cache.
	 *
	 * @param context The android context to use
	 * @param dataStoreOption The datastore type option to use.
	 * @return The Singleton Instance
	 */
	public static TwoStageCache getInstance(Context context, int dataStoreOption) {
		if(instance == null) {
			instance = new TwoStageCache();
			instance.context = context;
			instance.dataStoreOption = dataStoreOption;
			memCache = new LRUCache<String,Object>();
			diskCache = selectDataStore(context,"",dataStoreOption);
			instance = new TwoStageCache();
		}
		return instance;
	}

	/**
	 * LRU Config Instance Initialization
	 *
	 * This insance initialization allows you to select the persistence cache type,
	 * as well as provide configuration points for the in-memory cache.
	 *
	 * @param context The android context to use
	 * @param dataStoreOption The datastore type option to use.
	 * @param age The time (in milliseconds) that an entry can exist in-memory
	 * @param size The maximum number of entries in the in-memory cache.
	 * @return The Singleton Instance
	 */
	public static TwoStageCache getInstance(Context context, int dataStoreOption, long age, int size) {
		if(instance == null) {
			instance = new TwoStageCache();
			instance.context = context;
			instance.dataStoreOption = dataStoreOption;
			memCache = new LRUCache<String,Object>(age,size);
			diskCache = selectDataStore(context,"",dataStoreOption);
		}
		return instance;			
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
	 * @throws IllegalStateException Throws when an instance is not initialized
	 */
	public static Object get(String key) throws IllegalStateException {
	
		if(instance == null) {
			throw new IllegalStateException("Instance not initialized");
		}
	
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
	 * @throws IllegalStateException Throws when an instance is not initialized
	 */
	public static void put(String key,Object value) throws IllegalStateException {

		if(instance == null) {
			throw new IllegalStateException("Instance not initialized");
		}
		
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

	/**
	 * Remove an object from the cache
	 *
	 * This method will remove an entry in the cache
	 *
	 * @param key The key to insert/update
	 * @return The object stored under the key being removed for each cache
	 * @throws IllegalStateException Throws when an instance is not initialized
	 */
	public static Object[] remove(String key) throws IllegalStateException {
		if(instance == null) {
			throw new IllegalStateException("Instance not initialized");
		}

		Object[] o = new Object[] {memCache.remove(key),diskCache.get(key)};
		diskCache.delete(key);
		return o;
	}


	private static DataStore selectDataStore(Context context, String contextRoot, int option) {
		switch(option) {
			case FILESYSTEM_CACHE:
				return new FileSystemStore(context,contextRoot);
			case DATABASE_CACHE:
				return new DatabaseStore(context,contextRoot);
			default:
				return new FileSystemStore(context,contextRoot);
		}
	}
}
