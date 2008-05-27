package org.mobicents.slee.runtime.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.apache.log4j.Logger;

/**
 * 
 * Manages the synchronization of a tx local data with an actual dataset 
 *   when a tx ends. 
 * 
 * @author Ivelin Ivanov
 * @author Eduardo Martins
 *
 */
public class XACacheXAManager extends AbstractXAResource {

	/**
	 * The associated transaction 
	 */
	Transaction tx = null;
	
	/**
	 * The cache data set 
	 */
	XACache xaCache = null;
	
	private static Logger logger = Logger.getLogger(XACacheXAManager.class.getName());
	
	/**
	 * @param tx the associated tx 
	 * @param txLocalViews the map structure that needs to be cleaned
	 */
	XACacheXAManager(Transaction tx, XACache xacache) {
		assert tx != null : "transaction cannot be null";
		assert xacache != null : "xacache cannot be null";

		if(logger.isDebugEnabled()) {
			logger.debug("Registering as XAResource for tx[" + tx + "]");
		}
		
		this.tx = tx;
		this.xaCache = xacache;
	}
	
	/**
	 * Note: end() is invoked after commit() is invoked.
	 */
	public void end(Xid xid, int flags) throws XAException {
		if(logger.isDebugEnabled()) {
			logger.debug("Transaction end. tx[" + tx + "], xid[" + xid + "]");
		}
	}
	
	public void commit(Xid xid, boolean onePhase) throws XAException {
		Map txLocalView = xaCache.getTxLocalView(tx);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Transaction commit. tx[" + tx + "], xid[" + xid + "]");
		}
		
		if (txLocalView == null) {
			logger.warn("txLocalView is null for tx[" + tx + "], xid[" + xid + "]\n" +
					"This should never happen. \n" +
					"If a CacheXAManager was registered with a tx, \n" +
			"that is only because there was access to a map strucure in the tx context.");
			return;
		};
		
		Map actualMaps = xaCache.getActualMaps();
		
		try {
			// this synchronization is a temporary quick implementation to test the isolation correctness.
			// TODO: an optimized version should be implemented that only relies on entry level locking
			synchronized (actualMaps) {
				
				// iterate over all maps stored in a tx local view and 
				//   verify their data agaisnt the actual data
				boolean verificationFailed = false;
				Iterator mapsIter = txLocalView.entrySet().iterator();
				while (mapsIter.hasNext() && !verificationFailed) {
					Map.Entry entry = (Map.Entry) mapsIter.next();
					TxLocalMap nextLocalMap = (TxLocalMap)entry.getValue();
					Map actualMap = (Map)actualMaps.get(entry.getKey());				
					verificationFailed = !verifyMap(entry.getKey(), nextLocalMap, actualMap);				
				}				
				
				if (verificationFailed) {				
					throw new XAException(javax.transaction.xa.XAException.XA_HEURRB);
				} else {
					// else commit changes
					mapsIter = txLocalView.entrySet().iterator();
					while (mapsIter.hasNext()) {
						Map.Entry entry = (Map.Entry) mapsIter.next();
						TxLocalMap nextLocalMap = (TxLocalMap)entry.getValue();
						Map actualMap = (Map)actualMaps.get(entry.getKey());
						if (actualMap == null) {
							actualMap = new ConcurrentHashMap();
							actualMaps.put(entry.getKey(), actualMap);
						}
						if (nextLocalMap.isRemoved()) {							
							// the whole map is removed
							actualMaps.remove(entry.getKey());														
						} else {							
							// apply updates to dirty entries
							commitMapChanges(entry.getKey(), nextLocalMap, actualMap);							
						}
					}
				}			
			}
		}
		finally {
			// free up resources. the tx local view no longer 
			// needs to be registered in the global view list
			xaCache.removeTxLocalView(tx);
		}
	}
	
	/**
	 * verify that each entry in a tx local map
	 * can be committed to the actual map.
	 * The verification ensures that the version of an entry
	 * in the tx local map is newer than 
	 * the one committed in the actual data map.
	 * 
	 * @param txLocalMap tx local map to be verified
	 * @param actualMap
	 * @return true if the local map can be applied to the actual map without any conflicts  
	 */
	private boolean verifyMap(Object mapKey, TxLocalMap localMap, Map actualMap) {						
		// see if the tx local map has been marked for removal
		if (localMap.isRemoved()) {
			// if so, then there is no need to verify each individual map entry
			return true;
		} else { // the local map is not marked for removal
			if (actualMap == null) {
				if (localMap.isInCache()) {
					// a previous tx removed the map
					return false;
				}
				else {
					// new map
					return true;
				}
			}
			else {
				Set txLocalEntries = localMap.entrySet();
				Iterator iter = txLocalEntries.iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					VersionedEntry txLocalEntry = (VersionedEntry)entry.getValue();
					if (!txLocalEntry.isDirty()) {
						// the tx local entry is read only. Remove it from  the change set. 
						// There is no need for it to override the current actual entry
						iter.remove();					
					} else { // tx local entry is dirty. Lets see if it can be written to the actual map.					
						// actual map exists, lets verify for entry version conflict
						VersionedEntry actualEntry = (VersionedEntry)actualMap.get(entry.getKey());
						if (actualEntry != null && !txLocalEntry.isNewerVersion(actualEntry)) {							
							// if there is an actual entry and its not older than the local, 
							//   then we can't update the actual entry
							// ...but before failing, let's see if the values are the same
							//   in case that they are, then there is no need to fail							
							if (!txLocalEntry.getValue().equals(actualEntry.getValue())) {																
								if (logger.isInfoEnabled()) {
									logger.info("Failed to commit XACache writes in tx[" + tx + ".\n" +								
											"Reason: Entry version verification failed for Map[" + mapKey + "].\n" +
											"Reason: Committing entry is not newer version than the actual entry's version.\n" +
											" entry key[" + entry.getKey() + "]\n" + 
											" committing entry version[" + txLocalEntry.getVersion() + "]\n" +
											" committing entry value[" + txLocalEntry.getValue() + "]\n" +
											" actual entry version[" + actualEntry.getVersion() + "].\n" +
											" actual entry value[" + actualEntry.getValue() + "]"
									);
								}
								return false;
							}
							else {
								// same value, we can skip this one from commit
								iter.remove();
							}
						}
					}
				}
				// no conflict was detected. the map can be committed.
				return true;
			}		
		}
	}

	/**
	 * safely commit all changes from the tx local maps to the actual maps
	 * 
	 * @param txLocalMap tx local map to be verified
	 */
	private void commitMapChanges(Object mapKey, TxLocalMap txLocalMap, Map actualMap) {
		// lets update all dirty entries, if any
		Set txLocalEntries = txLocalMap.entrySet();
		Iterator iter = txLocalEntries.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			VersionedEntry txLocalEntry = (VersionedEntry)entry.getValue();
			VersionedEntry actualEntry = (VersionedEntry)actualMap.get(entry.getKey());
			if (actualEntry == null) {
				if (!txLocalEntry.isRemoved()) {
					// if there is no actual entry, and the local is not marked for removal
					// just make the tx local one actual					
					actualMap.put(entry.getKey(), txLocalEntry);
				}
			} else if (txLocalEntry.isRemoved()) {
				// if the tx local entry is marked for removal, then remove the actual entry				
				actualMap.remove(entry.getKey());
			} else if (!txLocalEntry.getValue().equals(actualEntry.getValue())) {				
				// if the local value is different than the actual, update the actual				
				actualMap.put(entry.getKey(), txLocalEntry);
			}
			// else the tx local entry was read only, no need to update the actual entry
		};
	}
	
	public void rollback(Xid xid) throws XAException {
		if(logger.isDebugEnabled()) {
			logger.debug("Transaction rollback. tx[" + tx + "], xid[" + xid + "]");
		}
		// free up resources. the tx local view no longer 
		//   needs to be registered in the global view list
		xaCache.removeTxLocalView(tx);
	}

}
