/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2007, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License (GPL) along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.runtime.cache;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.util.JndiRegistrationManager;

/**
 * 
 * <pre>
 *  A facility which allows transactional operations in memory space. 
 *  
 *  Note: Instances of XACache are thread safe.
 *  
 * </pre>
 * 
 * @author Ivelin Ivanov
 * 
 */
public class XACache {

	//private static Logger logger = Logger.getLogger(XACache.class.getName());

	// singleton
	private static XACache tmem = new XACache();

	/**
	 * The maps that hold the actual committed data
	 */
	private ConcurrentHashMap actualMaps = new ConcurrentHashMap();

	/**
	 * pairs of (ongoing transaction, transactional map)
	 */
	private ConcurrentHashMap txLocalViews = new ConcurrentHashMap();

	private static TransactionManager transactionManager;

	private XACache() {
		try {
			transactionManager = (SleeTransactionManager) JndiRegistrationManager.getFromJndi("slee/"
							+ SleeTransactionManager.JNDI_NAME);				
		} catch (Exception ex) {
			// ignore
		}	    
	}
	
	public static void setTransactionManager(TransactionManager txm) {
		if (txm == null) throw new NullPointerException("TransactionManager cannot be null");
		transactionManager = txm;
	}

	// returns singleton
	static XACache getInstance() {
		return tmem;
	}
	
	// solves serialization of singleton
	private Object readResolve() throws ObjectStreamException {
        return tmem;
    }

    // solves cloning of singleton
    protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
    }

	/**
	 * <pre>
	 *  
	 *  Returns a map wrapper instance, which can be used 
	 *  within the scope of an ongoing transaction to access the underlying cache map.
	 *  The returned instance cannot be reused in another transaction. 
	 *  
	 *  If a map with the given key has not already been created, this method creates a new instance.
	 *  
	 * </pre>
	 * 
	 * @param key
	 *            the unique key of the map in the cache name space
	 * @return a transaction local instance wrapper of the keyed cache map
	 * 
	 * @see
	 * 
	 */
	TxLocalMap getTxLocalMap(Object key) {
		if (key == null) throw new NullPointerException("key cannot be null");
 		
		Transaction tx = getTransaction();

		// get the tx local view of maps
		Map txLocalView = getTxLocalView(tx);
		
		// lookup the map under the given key in the tx local view
		TxLocalMap txlmap = (TxLocalMap)txLocalView.get(key);

		if (txlmap != null) {
			return txlmap;
		} else {			
			txlmap = new TxLocalMap(getActualMap(key) != null);
			
			txLocalView.put(key, txlmap);
			return txlmap;
		}
	}
	
	/**
	 * 
	 * Obtains the keyed map holding actual committed data  
	 * 
	 * @return the actual commited data Map
	 */
	Map getActualMap(Object key) {
		return (Map) actualMaps.get(key);
	}

	/**
	 * <pre>
	 *  
	 *  Returns a Set wrapper instance, which can be used 
	 *  within the scope of an ongoing transaction to access the underlying cache set.
	 *  The returned instance cannot be reused in another transaction. 
	 *  
	 *  If a set with the given key has not already been created, this method creates a new instance.
	 *  It is based on an underlying Map stored under the given key;
	 *  
	 * </pre>
	 * 
	 * @param key
	 *            the unique key of the map in the cache name space
	 * @return a transaction local instance wrapper of the keyed cache set
	 * 
	 * @see
	 * 
	Set getSet(Object key) {
		Map map = getMap(key);
		Set set = new CacheableSet(map);
		return set;
	}
	 */
	
	
	/**
	 * 
	 * Return the current contextual JTA transaction if there is one.
	 * 
	 * @throws IllegalStateException
	 *             1) if there is no transaction available or 2) there is tx,
	 *             but its not in STATUS_ACTIVE or STATUS_MARKED_ROLLBACK state.
	 */
	private Transaction getTransaction() {
		Transaction tx = null;
		try {
			tx = transactionManager.getTransaction();
			if (tx == null)
				throw new IllegalStateException(
						"Failed to obtain transaction. tx is null.");
		} catch (SystemException e) {
			//throw new IllegalStateException("Failed to obtain transaction due to SystemException.", e); //This is java 1.5 cosntructor
			throw new IllegalStateException("Failed to obtain transaction due to SystemException.["+e+"]");
		}

		try {
			if (!((tx.getStatus() == Status.STATUS_ACTIVE) || (tx.getStatus() == Status.STATUS_COMMITTING) || ((tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)))) {
				throw new IllegalStateException("There is no active tx, tx["
						+ tx + "] is in state: " + tx.getStatus());
			}
		} catch (SystemException e) {
			//throw new IllegalStateException("Failed to check transaction status for tx[" + tx+ "] due to SystemException.", e); //This is java 1.5 constructor
			throw new IllegalStateException("Failed to check transaction status for tx[ " + tx+ " ] due to SystemException.["+e+"]");
		}
		return tx;
	}

	Map getTxLocalView(Transaction tx) {
		Map txLocalView = (Map)txLocalViews.get(tx);
		if (txLocalView == null) {
			// first time called within the current tx scope
			//   a new tx local view needs to be created
			txLocalView = new HashMap();
			txLocalViews.put(tx, txLocalView);

			// a tx call back needs to be register to ensure 
			//   that the tx local view will be cleaned when the tx ends
	    	XACacheXAManager xares = new XACacheXAManager(tx, this);
	    	try {
				tx.enlistResource(xares);
			} catch (Exception e) {
				//throw new IllegalStateException("Failed to enlist XAMapResource in tx: " + tx, e);//This is java 1.5 constructor
				throw new IllegalStateException("Failed to enlist XAMapResource in tx: [ "+tx+" ]["+e+"]");
			}
		}
		return txLocalView;
	}
	
	void removeTxLocalView(Transaction tx) {
		txLocalViews.remove(tx);
	}

	Map getActualMaps() {
		return actualMaps;
	}
	
	public static String dumpState() {
		return tmem.toString();
	}
	
	@Override
	public String toString() {
		return 	"XACache: " +
		"\n+-- Cacheable Maps: " + actualMaps.size();
	}
}
