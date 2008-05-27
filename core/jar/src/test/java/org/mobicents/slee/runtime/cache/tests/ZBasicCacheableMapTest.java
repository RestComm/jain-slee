package org.mobicents.slee.runtime.cache.tests;

import javax.transaction.Transaction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.XACache;

public class ZBasicCacheableMapTest extends TestCase {
	
	private TransactionManagerMockup txm = null;
	private CacheableMap cmap = null; 

	
	public ZBasicCacheableMapTest(String name)
	{
		super(name);
	}
	public static Test suite()
	{
		return new TestSuite(ZBasicCacheableMapTest.class);
	}
	protected void setUp() throws Exception {
		super.setUp();
		txm = new TransactionManagerMockup();
		Transaction tx = new TransactionMockup();
		txm.pushCurrentTransaction(tx);
		XACache.setTransactionManager(txm);
		cmap = new CacheableMap("testMap");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testContainsUnexistentKey() {
		boolean b = cmap.containsKey("unsetKey");
		assertFalse("should return false", b);
	}
	
	public void testContainsExistingKey() {
		cmap.put("key1", new Object());
		boolean b = cmap.containsKey("key1");
		assertTrue("should return true", b);
	}

	public void testGetExistingEntry() {
		Object someObj = new Object();
		cmap.put("key1", someObj);
		Object o = cmap.get("key1");
		assertEquals("should return the object that was put in", someObj, o);
	}

	public void testGetUnsetEntry() {
		Object o = cmap.get("unsetKey");
		assertNull("CacheableMap.get('unsetKey') should return null", o);
	}
		
	public void testPutNullKey() {
		try {
			cmap.put(null, "something");
			fail("null keys should not be allowed");
		} catch (NullPointerException npe) {
			// expected. pass.
		}
	}
		
	public void testPutNullValue() {
			cmap.put("key1", null);
			// null value is allowed
	}
	
	public void testGetAfterRemove() {
		try {
			cmap.remove();
			cmap.get("key1");
			fail("get() after remove is illegal.");
		} catch (IllegalStateException e) {
			// expected. pass.
		}
	}

	public void testPutAfterRemove() {
		cmap.put("key1", "v1");
		cmap.remove();
		cmap.put("key2", "v2");
		Object oldEntry = cmap.get("key1");
		assertNull("no gargbage should be found in a cmap after revival.", oldEntry);
		Object newEntry = cmap.get("key2");
		assertEquals("the new entry should be there.", "v2", newEntry);
	}

}


