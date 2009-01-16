package org.mobicents.slee.runtime.cache.tests;

import java.util.Map;

import javax.transaction.Transaction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.XACache;

public class ZConcurrencyCacheableMapTest extends TestCase {
	
	private TransactionManagerMockup txm = null;
	private CacheableMap cmap = null; 

	
	
	public ZConcurrencyCacheableMapTest(String name)
	{
		super(name);
	}
	public static Test suite()
	{
		return new TestSuite(ZConcurrencyCacheableMapTest.class);
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
	
	public void testWriteInTx1CommitReadInTx2() throws Exception {
		
		Object key1 = "key-testWriteInTx1CommitReadInTx2";

		Object val1 = "value1";
		cmap.put(key1, "value1");
		
		Transaction tx = txm.getTransaction();
		tx.commit();
		
		txm.pushCurrentTransaction(new TransactionMockup());

		Map cmap2 = new CacheableMap("testMap");
		Object val2 = cmap2.get(key1);

		assertEquals("entry written in tx 1 should be visible in tx 2", val1, val2);
	}

	public void testPutRemoveCommitRead() throws Exception {
		Object key1 = "key1-testPutRemoveCommitRead";
		Object key2 = "key2-testPutRemoveCommitRead";

		Object val1 = "value1";
		Object val2 = "value2";
		cmap.put(key1, val1);
		cmap.put(key2, val2);
		cmap.remove();
		
		Transaction tx = txm.getTransaction();
		tx.commit();
		
		txm.pushCurrentTransaction(new TransactionMockup());

		Map cmap2 = new CacheableMap("testMap");
		val1 = cmap2.get(key1);
		val2 = cmap2.get(key2);

		assertNull("entry written in tx 1 in a map that is removed in tx 1 should not be be visible in tx 2", val1);
		assertNull("entry written in tx 1 in a map that is removed in tx 1 should not be be visible in tx 2", val2);
	}

	public void testPutNullCommitRead() throws Exception {
		Object key1 = "key1-testPutRemoveCommitRead";

		cmap.put(key1, null);
		
		Transaction tx = txm.getTransaction();
		tx.commit();
		
		txm.pushCurrentTransaction(new TransactionMockup());

		Map cmap2 = new CacheableMap("testMap");

		boolean nullValueFound = cmap2.containsKey(key1);
		assertTrue("null value entry written in tx 1 should be visible as null in tx 2", nullValueFound);

		Object val = cmap2.get(key1);
		assertNull("null value entry written in tx 1 should exist as null in tx 2", val);

	}

	
	public void testWriteInTx1RollbackReadInTx2() throws Exception {
		
		Object val1 = "value1";
		Object key1 = "key-testWriteInTx1RollbackReadInTx2";

		boolean found1 = cmap.containsKey(key1);
		assertFalse("key1 should not be found, because it hasn't been inserted yet.", found1);

		cmap.put("key1", val1);
		
		Transaction tx = txm.getTransaction();
		tx.rollback();
		
		txm.pushCurrentTransaction(new TransactionMockup());

		Map cmap2 = new CacheableMap("testMap");
		boolean found2 = cmap2.containsKey(key1);

		assertFalse("entry written in tx 1, which then rolled back, should not be visible in tx 2", found2);
		
	}

	public void testPhantomReadsDisallowed() throws Exception {
		
		Object key1 = "key1-testPutRemoveCommitRead";
		Object key2 = "key2-testPutRemoveCommitRead";

		Object val1 = "value1";
		Object val2 = "value2";

		cmap.setAllowPhantomReads(false);
		
		cmap.put(key1, val1);
		
		boolean real = cmap.containsKey(key1);
		assertTrue("key 1 was written in tx 1 and it shoud be still visible", real);

		Transaction tx2 = new TransactionMockup();
		
		txm.pushCurrentTransaction(tx2);

		Map cmap2 = new CacheableMap("testMap");
		cmap2.put(key2, val2);
		
		tx2.commit();
		
		txm.popCurrentTransaction();
		
		boolean phantom = cmap.containsKey(key2);
		assertFalse("key 2 was written in tx 2 after tx 1 started and it shoud be therefore invisible", phantom);
		
	}
	
	public void testWriteRemoveCommitRead() throws Exception {
		
		Object key1 = "key1-testPutRemoveCommitRead";

		Object val1 = "value1";

		cmap.put(key1, val1);
		cmap.remove();
		
		Transaction tx = txm.getTransaction();
		tx.commit();

		Transaction tx2 = new TransactionMockup();
		txm.pushCurrentTransaction(tx2);

		Map cmap2 = new CacheableMap("testMap");
		boolean removedFound = cmap2.containsKey(key1);
		
		tx2.commit();
		txm.popCurrentTransaction();
		
		assertFalse("key 1 was written and removed in tx 1 which committed, it should not be visible in tx 2 ", removedFound);
		
	}
	
}
