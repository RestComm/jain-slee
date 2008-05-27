package org.mobicents.slee.runtime.cache.tests;

import javax.transaction.Transaction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.XACache;
import org.mobicents.slee.runtime.cache.XACacheTestViewer;

public class ZCacheableMapCreationRollbackTest extends TestCase {
	
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
		return new TestSuite(ZCacheableMapCreationRollbackTest.class);
		
	}
		
	/**
	 *  
	 * @throws Exception
	 */
	public void test() throws Exception {		
				
		Transaction  tx;
		CacheableMap map;		
		
		// create transaction
		txm.pushCurrentTransaction(new TransactionMockup());
		
		tx=txm.getTransaction();
		
		if (!XACacheTestViewer.getXACacheMap().isEmpty()) {
			throw new RuntimeException("XACache not empty before running test. keyset: "+XACacheTestViewer.getXACacheMap().keySet());
		}
		
		// put a new map in cache
		map=new CacheableMap("MAP");		
		map.put("KEY","STRINGENTRY");		
		
		tx.rollback();
		
		if (!XACacheTestViewer.getXACacheMap().isEmpty()) {
			throw new RuntimeException("XACache not empty after running test. keyset: "+XACacheTestViewer.getXACacheMap().keySet());
		}
		
	}
		
}
