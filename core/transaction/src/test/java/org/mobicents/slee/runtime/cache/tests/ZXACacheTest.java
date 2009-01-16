package org.mobicents.slee.runtime.cache.tests;

import javax.transaction.Transaction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.cache.XACache;

public class ZXACacheTest extends TestCase {
	
	private TransactionManagerMockup txm = null;

	protected void setUp() throws Exception {
		super.setUp();
		txm = new TransactionManagerMockup();
		Transaction tx = new TransactionMockup();
		txm.pushCurrentTransaction(tx);
		XACache.setTransactionManager(txm);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public ZXACacheTest(String name)
	{
		super(name);
	}
	public static Test suite()
	{
		return new TestSuite(ZXACacheTest.class);
	}
	
	
	
	public void testNewCacheableMapInstance() {
		new CacheableMap("mapKey1");
	}
		
	public void testNewCMapForNullKey() {
		try {
			new CacheableMap(null);
			fail("new CacheableMap(null) should throw NPE. Map key cannot be null.");
		} catch (NullPointerException e) {
			// this is expected. pass.
		}
	}
	
	public void testNewCSet() {
		new CacheableSet("setKey1");
	}
	
	public void testNewCSetForNullKey() {
		try {
			new CacheableSet(null);
			fail("new CacheableSet(null) should throw NPE. Set key cannot be null.");
		} catch (NullPointerException e) {
			// this is expected. pass.
		}
	}
	
	public void testGetMapWithoutTransaction() {
		txm.pushCurrentTransaction(null);
		new CacheableMap("key1");
		// if should be possible to instantiate CacheableMap outside tx context
	}

}
