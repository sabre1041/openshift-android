package com.openshift.android.util;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * This class provides an in-memory Least Recently Used (LRU) Cache. 
 *
 * <p>This cache will evict the eldest entry if the cache has been filled.
 * Additionally, an entry will be evicted if it is older than the expiry period.
 *
 * <p>You may supply a callback that will be called to notify of any type evicted entries.
 *
 * @author Joey Yore
 * @version 1.0
 */
public class LRUCache<K,V> {
	
 	/** Default maximum number of entries is 128*/ 
	private static final int DEFAULT_MAX_ENTRIES = 128;
 	
	/** Default expiration time is 600000ms or 10min */
	private static final long DEFAULT_AGE = 600000;

	private Object lock = new Object();
	private long age;
	private HashMap<K,CacheItem<V>> memCache;
	

	/**
 	* Public interface for providing a callback
 	*
 	* The notify method is called on eviction
 	*/
	public interface EvictedNotificationCallback<K,V> {
		/**
		 * The method that gets called when an entry gets evicted
		 *
		 * @param key The key of the entry evicted
		 * @param value The payload of the entry 
		 */
		public void notify(K key,V value);
	}

	
	/**
 	* Default Constructer - Will setup default eviction policies and disable the callback
 	*/
	public LRUCache() {
		this(DEFAULT_AGE,DEFAULT_MAX_ENTRIES,null);
	}


	/**
	 * Age Constructor - Will allow for modification of the age policy
	 *
	 * @param age The expiration time that an entry should live in milliseconds
	 */
	public LRUCache(long age) {
		this(age,DEFAULT_MAX_ENTRIES,null);
	}

	
	/**
	 * Policy Constructor - Will allow for modification of age and space policies
	 *
	 * @param age The expiration time that an entry should live in milliseconds
	 * @param maxEntries The maximum number of entries to store
	 */
	public LRUCache(long age,int maxEntries) {
		this(age,maxEntries,null);
	}

	
	/**
	 * Callback Constructor - Will allow for complete configuration of polices and the ability to supply a callback for handling evicted entries
	 *
	 * @param age The expiration time that an entry should live in milliseconds
	 * @param maxEntries The maximum number of entries to store
	 * @param callback An implementation class of the EvictedNotificationCallback interface. The notify() method is called when an entry is evicted
	 */
	public LRUCache(long age, int maxEntries, final EvictedNotificationCallback<K,V> callback) {
		this.age = age;
		final int MAX_ENTRIES = maxEntries;
		this.memCache = new LinkedHashMap<K,CacheItem<V>>(maxEntries+1,0.75f,true) {
			private static final long serialVersionUID = 1L;
			
			public boolean removeEldestEntry(Map.Entry<K, CacheItem<V>> eldest) {
				long age = System.currentTimeMillis() - eldest.getValue().birth;
				
				if((size() > MAX_ENTRIES) || (age > LRUCache.this.age)) {
					if(callback != null) {
						callback.notify(eldest.getKey(),eldest.getValue().payload);
					}
					return true;
				}
				return false;
			}
		};
	}


	/**
	 * Add a new entry into the cache
	 *
	 * @param key The key name [unique] to store the value under
	 * @param value The value to store under the key
	 */
	public void put(K key, V value) {
		synchronized (lock) {
			memCache.put(key, new CacheItem<V>(value));
		}
	}

	
	/**
	 * Retrieve an item from the cache
	 *
	 * @param key The key to lookup
	 * @return The value of the stored entry. Returns null if key does not exist
	 */
	public V get(K key) {
		synchronized (lock) {
			CacheItem<V> item = getItem(key);
			return item == null ? null : item.payload;
		}
	}

	
	/**
	 * Remove an item from the cache (force eviction)
	 *
	 * @param key The key for the entry to remove
	 * @return The value of the entry that is removed
	 */
	public V remove(K key) {
		synchronized (lock) {
			CacheItem<V> item = memCache.remove(key);
			if(item != null) {
				return item.payload;
			} else {
				return null;
			}
		}
	}

	
	/**
	 * Get the number of entries stored in the cash
	 *
	 * @return The number of entries stored in the cash
	 */
	public int size() {
		synchronized (lock) {
			return memCache.size();
		}
	}
	
	private CacheItem<V> getItem(K key) {
		CacheItem<V> item = memCache.get(key);
		if(item == null) {
			return null;
		}
		item.touch();
		return item;
	}
	
	private static class CacheItem<T> {
		long birth;
		T payload;
		
		
		CacheItem(T payload) {
			this.birth = System.currentTimeMillis();
			this.payload = payload;
		}
		
		public void touch() {
			this.birth = System.currentTimeMillis();
		}
	}
}
