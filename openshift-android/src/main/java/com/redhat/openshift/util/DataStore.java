package com.redhat.openshift.util;


public interface DataStore {
	public void create(String key, Object o);
	public Object get(String key);
	public int update(String key, Object o);
	public void delete(String key);
}
