package com.openshift.android.cache;

/**
 * An interface that provides an access contract for anything
 * that is a data store
 *
 * @author Joey Yore
 * @version 1.0
 */
public interface DataStore {

	/** 
 	* Provides a way to create an entry in a datastore
 	*
 	* @param key The key name to store the entry under
 	* @param o The entry payload
 	*/
	public void create(String key, Object o);

	/**
	 * Provides a way to get a stored entry from the store
	 *
	 * @param key The key name of the entry to lookup
	 * @return The entry payload or null if it does not exists
	 */
	public Object get(String key);

	/**
	 * Provides a way to update a stored payload
	 *
	 * @param key The key name in which to update
	 * @param o The updated payload to replace the new
	 * @return The number of entries affected. Should be 0 or 1.
	 */
	public int update(String key, Object o);

	/**
	 * Provides a way to delete an entry from the store
	 *
	 * @param key The key name of the entry to delete
	 */
	public void delete(String key);
}
