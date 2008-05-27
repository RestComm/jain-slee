package org.mobicents.slee.runtime.cache.tests;

import javax.transaction.Transaction;
import javax.transaction.xa.XAException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.XACache;
import org.mobicents.slee.runtime.cache.XACacheTestViewer;

public class ZConcurrentRemoveThenModifyCacheableMapTest extends TestCase {
	
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
		return new TestSuite(ZConcurrentRemoveThenModifyCacheableMapTest.class);
		
	}
		
	/**
	 *  
	 * @throws Exception
	 */
	public void test() throws Exception {		
				
		Transaction  tx1,tx2,tx3;
		CacheableMap map1,map2,map3;		
		
		// create transaction
		txm.pushCurrentTransaction(new TransactionMockup());
		
		tx1=txm.getTransaction();
		
		// put a new map in cache
		map1=new CacheableMap("MAP");		
		map1.put("KEY","STRINGENTRY");		
		
		tx1.commit();
		
		// create transaction
		txm.pushCurrentTransaction(new TransactionMockup());
		
		tx2=txm.getTransaction();
		map2=new CacheableMap("MAP");		
		map2.remove("KEY");
		
		
		txm.pushCurrentTransaction(new TransactionMockup());
		
		tx3=txm.getTransaction();
		map3=new CacheableMap("MAP");
		map3.remove("KEY");
		map3.remove();
		tx3.commit();
		
		txm.popCurrentTransaction();
		
		boolean rb = true;
		try {
			tx2.commit();
			rb = false;
		}
		catch (Exception e) {
			
		}
		
		if(!rb) {
			throw new RuntimeException("Tx2 committed");
		}
		/*
		if (!XACacheInspector.getXACacheMap().isEmpty()) {
			throw new RuntimeException("XACache not empty after running test. keyset: "+XACacheInspector.getXACacheMap().keySet());
		}
		*/
		
	}
		
}
