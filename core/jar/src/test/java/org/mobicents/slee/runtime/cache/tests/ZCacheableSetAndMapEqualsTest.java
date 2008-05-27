package org.mobicents.slee.runtime.cache.tests;

import javax.transaction.Transaction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.CacheableSet;
//import org.mobicents.slee.runtime.cache.TxLocalMap;
import org.mobicents.slee.runtime.cache.XACache;

/**
 * 
 * Tests introduced in issue 179. For more info check 
 * https://mobicents.dev.java.net/issues/show_bug.cgi?id=179
 * 
 * @author Eduardo Martins
 * @author Bartosz Baranowski
 *
 */
public class ZCacheableSetAndMapEqualsTest extends TestCase {

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
		return new TestSuite(ZCacheableSetAndMapEqualsTest.class);
		
	}
		
/*
 	 // requires public TxLocalMap
	 public void testTxLocalMapEquality()
	 {
		
	 //This one is really simple - ony two cases to check - if is removed and contianed entry set
	 // no tx need here
	 TxLocalMap txlm_1,txlm_2;
				
	 txlm_1=new TxLocalMap();
	 txlm_2=new TxLocalMap();
				
	 txlm_1.put("key_1", "value_1");
	 txlm_1.put("key_2", "value_2");
	 txlm_1.put("key_3", "value_3");
				
	 txlm_2.put("key_1", "value_1");
	 txlm_2.put("key_2", "value_2");
	 
	 assertTrue("TxLocalMap entries should not equal.", !txlm_1.equals(txlm_2));
	 
	 txlm_2.put("key_3", "value_3");
		
	 //Same value, it should return true
		
	 assertTrue("TxLocalMap entries should equal.", txlm_1.equals(txlm_2));
		
	 txlm_1.remove();
		
	 assertFalse("One of tx map is going to be removed, they should not equal each other now", txlm_1.equals(txlm_2));
				
	 txlm_2.remove();
		
	 assertTrue("Both tx maps are going to be removed, they should equal each other now", txlm_1.equals(txlm_2));
	 }
*/
	
	/**
	 * Simple test to test equals method...
	 * 
	 */
	public void testCacheableMapEquality() throws Exception {

		// We need two concurrent tx

		Transaction tx1, tx2, currentTX;
		CacheableMap map1, map2;

		tx1 = new TransactionMockup();
		tx2 = new TransactionMockup();

		txm.pushCurrentTransaction(tx1);
		txm.pushCurrentTransaction(tx2);
		txm.pushCurrentTransaction(new TransactionMockup());

		currentTX = txm.getTransaction();

		map1 = new CacheableMap("TEST_1_MAP");
		map1.put("ENTRY", "VALUE");
		currentTX.commit();
		txm.popCurrentTransaction();
		
		
		//Now there is empty map;
		tx1=txm.getTransaction();
		map1=new CacheableMap("TEST_1_MAP");
		txm.popCurrentTransaction();
		tx2=txm.getTransaction();
		map2=new CacheableMap("TEST_1_MAP");
		
		//Both are have one etry, but local map is empty 
		
		assertTrue("Maps should be equal even if they do not contain local copies of entires",map1.equals(map2));
				
		//Now we perform some operation, so local map gets copies of entries
		
		map1.containsKey("ENTRY");
		
		assertTrue("Maps should be equal even if one contains local copy of entries",map1.equals(map2));
		assertTrue("Maps should be equal even if one contains local copy of entries",map2.equals(map1));
		
		map2.containsKey("ENTRY");
		
		assertTrue("Maps should be equal.",map1.equals(map2));
		
		tx1.commit();
		map2.remove();
		tx2.commit();		
	}

	/**
	 * Simple test to test equals method...
	 * 
	 */
	public void testCacheableSetEquality() throws Exception {
		
		
//		 We need two concurrent tx

		Transaction tx1, tx2, currentTX;
		CacheableSet set1, set2;
		
		txm.pushCurrentTransaction(new TransactionMockup());
		txm.pushCurrentTransaction(new TransactionMockup());
		txm.pushCurrentTransaction(new TransactionMockup());

		currentTX = txm.getTransaction();

		set1 = new CacheableSet("TEST_1_SET");
		set1.add("ENTRY");
		currentTX.commit();
		txm.popCurrentTransaction();
				
		//Now there is empty set;
		tx1=txm.getTransaction();
		set1=new CacheableSet("TEST_1_SET");
		txm.popCurrentTransaction();
		tx2=txm.getTransaction();
		set2=new CacheableSet("TEST_1_SET");
		
		//Both are have one etry, but local set is empty 
		
		assertTrue("Sets should be equal even if they do not contain local copies of entires",set1.equals(set2));
		
		//Now we perform some operation, so local map gets copies of entries
		
		set1.contains("ENTRY");
		
		assertTrue("Sets should be equal even if one contains local copy of entries",set1.equals(set2));
		assertTrue("Sets should be equal even if one contains local copy of entries",set2.equals(set1));
		
		set2.contains("ENTRY");
		
		assertTrue("Sets should be equal.",set1.equals(set2));
		
		tx1.commit();
		set2.remove();
		tx2.commit();
				
	}

	/**
	 * Simple test to test equals method...
	 * 
	 */
	public void testCacheableSetConcurrentAccess() throws Exception {		
		
		Transaction  tx1,tx2,tx3;
		CacheableSet set;
		CacheableMap setEntry;		
						
		txm.pushCurrentTransaction(new TransactionMockup());
		tx1=txm.getTransaction();
		
		// put a new map in cache		
		setEntry = new CacheableMap("SETENTRY");
		setEntry.put("KEY","STRINGENTRY");
		set=new CacheableSet("SET");
		set.add(setEntry);
				
		
		txm.pushCurrentTransaction(new TransactionMockup());
		tx2=txm.getTransaction();
		
		// concurrent tx, put an equal set in cache		
		setEntry = new CacheableMap("SETENTRY");
		setEntry.put("KEY","STRINGENTRY");
		set=new CacheableSet("SET");
		set.add(setEntry);
		
		
		txm.pushCurrentTransaction(new TransactionMockup());
		tx3=txm.getTransaction();
						
		// put a not equal set in cache		
		setEntry = new CacheableMap("SETENTRY");
		setEntry.put("KEY","ANOTHERSTRINGENTRY");
		set=new CacheableSet("SET");
		set.add(setEntry);						
		
		// shouldn't fail commit
		tx1.commit();				
		
		// shouldn't fail commit		
		tx2.commit();		
		
		// should fail commit
		boolean exceptionThrown = false;
		try {			
			tx3.commit();
		}
		catch (Exception e) {		
			exceptionThrown = true;
		}				
		
		assertTrue("Exception should be thrown when commiting a different set on a not newer tx",exceptionThrown);
		
		txm.pushCurrentTransaction(new TransactionMockup());
		
		set=new CacheableSet("SET");		
		set.remove();
		
		txm.getTransaction().commit();
				
	}
				

	/**
	 * Simple test to test equals method...
	 * 
	 */
	public void testCacheableMapConcurrentAccess() throws Exception {		
		
		Transaction  tx1,tx2,tx3;
		CacheableMap map,mapEntry;		
						
		txm.pushCurrentTransaction(new TransactionMockup());
		tx1=txm.getTransaction();
		
		// put a new map in cache
		map=new CacheableMap("MAP");
		mapEntry = new CacheableMap("MAPENTRY");
		map.put("KEY",mapEntry);
		mapEntry.put("KEY","STRINGENTRY");		
		
		txm.pushCurrentTransaction(new TransactionMockup());
		tx2=txm.getTransaction();
		
		// concurrent tx, should be version of mapentry, put an equal map in cache
		map=new CacheableMap("MAP");
		mapEntry = new CacheableMap("MAPENTRY");
		map.put("KEY",mapEntry);
		mapEntry.put("KEY","STRINGENTRY");
		
		txm.pushCurrentTransaction(new TransactionMockup());
		tx3=txm.getTransaction();
						
		// put a not equal map in cache
		map=new CacheableMap("MAP");
		mapEntry = new CacheableMap("MAPENTRY");
		map.put("KEY",mapEntry);		
		mapEntry.put("KEY","ANOTHERSTRINGENTRY");		
		
		// shouldn't fail commit
		tx1.commit();		
		
		// shouldn't fail commit		
		tx2.commit();
		
		// should fail commit
		boolean exceptionThrown = false;
		try {			
			tx3.commit();
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		
		assertTrue("Exception should be thrown when commiting a different map on a not newer tx",exceptionThrown);
		
		txm.pushCurrentTransaction(new TransactionMockup());
		
		map=new CacheableMap("MAP");		
		map.remove();
		
		txm.getTransaction().commit();				
	}
		
}
