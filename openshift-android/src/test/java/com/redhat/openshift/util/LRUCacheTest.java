package com.redat.openshift.util;

import org.junit.Test;
import org.junit.Assert;

public class LRUCacheTest {


	@Test
	public void basicOps() {
		System.out.println("\tTesting Basic Ops");

		LRUCache<String,String> cache = new LRUCache<String,String>();
		cache.put("test","abc");
		Assert.assertEquals(cache.get("test"),"abc");
		Assert.assertEquals(cache.remove("test"),"abc");
		Assert.assertEquals(cache.get("test"),null);
	}

	@Test
	public void timeEviction() {
		System.out.println("\tTesting Time Based Eviction");
		LRUCache<String,String> cache = new LRUCache<String,String>(100);

		cache.put("test1","abc");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			Assert.fail();
		}
		cache.put("test2","def");
		Assert.assertEquals(cache.get("test1"),null);
	}
	

	@Test
	public void spaceEviction() {
		System.out.println("\tTesting Space Based Eviction");
		LRUCache<String,String> cache = new LRUCache<String,String>(600000,2);

		cache.put("test1","abc");
		cache.put("test2","def");
		cache.put("test3","ghi");
		cache.put("test4","jkl");
		Assert.assertEquals(cache.get("test1"),null);
	}

	

	@Test
	public void callback() {
		System.out.println("\tTesting Callback");
		LRUCache<String,String> cache = new LRUCache<String,String>(600000,2,new cb<String,String>());
		
		cache.put("TESTK","TESTV");
		cache.put("test1","def");
		cache.put("test2","ghi");
	}
}

class cb<K,V> implements LRUCache.EvictedNotificationCallback<K,V> {

		private static final String KEY = "TESTK";
		private static final String VAL = "TESTV";

		public void notify(K key, V val) {
			System.out.println("\t\tCallBack Has Been Called");
			Assert.assertEquals(key,KEY);
			Assert.assertEquals(val,VAL);
		}
	}
