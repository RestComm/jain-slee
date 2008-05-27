package org.mobicents.slee.runtime.cache.tests;

import javax.transaction.Transaction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.XACache;

/**
 * 
 * Tests optimization to reduce conflict scenarios.If tx1 never
 * saw what's in the map (by reading or writing from/to the map) 
 * before tx2 committed, it is the same as if tx1 started after
 * tx2 committed.
 * For more info see issue 197 @ 
 * https://mobicents.dev.java.net/issues/show_bug.cgi?id=197
 * @author Eduardo Martins
 *
 */
public class ZConcurrentTxOptimizationTest extends TestCase {

	private TransactionManagerMockup txm = null;

	protected void setUp() throws Exception {
		super.setUp();
		txm = new TransactionManagerMockup();
		XACache.setTransactionManager(txm);

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public static Test suite()
	{
		return new TestSuite(ZConcurrentTxOptimizationTest.class);
		
	}
		
	/**
	 * Tests concurrent txs can't commit different values. 
	 * @throws Exception
	 */
	public void testBadIsolationProof() throws Exception {		
				
		Transaction  tx1,tx2;
		CacheableMap map;		
		
		// create 2 transactions
		txm.pushCurrentTransaction(new TransactionMockup());
		txm.pushCurrentTransaction(new TransactionMockup());
		
		tx1=txm.getTransaction();
		
		// put a new map in cache
		map=new CacheableMap("MAP");		
		map.put("KEY","STRINGENTRY");		
		
		// shouldn't fail commit
		tx1.commit();
		
		txm.popCurrentTransaction();
		tx2=txm.getTransaction();
		
		// concurrent tx, try to put a diff map in cache
		map=new CacheableMap("MAP");
		map.put("KEY","ANOTHERSTRINGENTRY");
		
		// shouldn't fail commit because tx state is not corrupted
		tx2.commit();		
		
		txm.pushCurrentTransaction(new TransactionMockup());
		
		map=new CacheableMap("MAP");		
		map.remove();
		
		txm.getTransaction().commit();
		
	}
		
}
