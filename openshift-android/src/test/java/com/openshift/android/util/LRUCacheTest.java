package com.openshift.android.util;

import com.openshift.android.util.LRUCache;

import junit.framework.TestCase;


public class LRUCacheTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBasicOps() {
		System.out.println("\tTesting Basic Ops");

		LRUCache<String,String> cache = new LRUCache<String,String>();
		cache.put("test","abc");
		assertEquals(cache.get("test"),"abc");
		assertEquals(cache.remove("test"),"abc");
		assertEquals(cache.get("test"),null);
	}

	public void testTimeEviction() {
		System.out.println("\tTesting Time Based Eviction");
		LRUCache<String,String> cache = new LRUCache<String,String>(100);

		cache.put("test1","abc");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			fail();
		}
		cache.put("test2","def");
		assertEquals(cache.get("test1"),null);
	}
	

	public void testSpaceEviction() {
		System.out.println("\tTesting Space Based Eviction");
		LRUCache<String,String> cache = new LRUCache<String,String>(600000,2);

		cache.put("test1","abc");
		cache.put("test2","def");
		cache.put("test3","ghi");
		cache.put("test4","jkl");
		assertEquals(cache.get("test1"),null);
	}

	
	public void testCallback() {
		System.out.println("\tTesting Callback");
		LRUCache<String,String> cache = new LRUCache<String,String>(600000,2,new cb<String,String>());
		
		cache.put("TESTK","TESTV");
		cache.put("test1","def");
		cache.put("test2","ghi");
	}


	private class cb<K,V> implements LRUCache.EvictedNotificationCallback<K,V> {

		private static final String KEY = "TESTK";
		private static final String VAL = "TESTV";

		public void notify(K key, V val) {
			System.out.println("\t\tCallBack Has Been Called");
			assertEquals((String)key,KEY);
			assertEquals((String)val,VAL);
		}
	}
}
