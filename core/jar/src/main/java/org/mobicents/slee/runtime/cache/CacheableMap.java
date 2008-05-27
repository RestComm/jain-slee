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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * Map which allows concurrent access from multiple transactions. 
 * It implements OPTIMISTIC LOCKING using the MVCC algorithm with SNAPHOST ISOLATION. 
 * 
 * Note: Snapshot isolation allows WRITE SKEW, which is a weakness that can be overcome by updating a well known entry to force version conflict.
 * 
 * Note: Transaction Isolation level is similar to ANSI REPEATABLE_READ
 * 
 * Note: This class is NOT thread safe. Each transaction needs to obtain its own instance of this class.
 * It is assumed that code executing within the context of a single transaction is sequential.
 * 
 * Note: This class depends on the Java Ttransactions API (JTA).
 * 
 * Isolation use cases for parallel transactions. Neither tx1 nor tx2 commits before the other one begins:
 * 1) tx1: (alice--, bob++); tx2: (alice--, charlie++) => write conflict on alice; one of the tx rolls back
 * 2) tx1: (a--, b++, a.remove); tx2: (a--, c++) => sucess, write skew! To prevent this, both tx1 and tx2 need to force conflict via common field update (d++)
 * 3) tx1: (a.remove); tx2: (a++) => 
 * 		a) If tx1 commits first, tx2 succeeds; To prevent this, both tx1 and tx2 need to force conflict via common field update (d++)
 *      b) If tx2 commits first, tx1 fails due to update version conflict;
 * 4) tx1: (read a, b++); tx2: (read b, a++) => both succeed; To prevent this, both tx1 and tx2 need to force conflict via common field update (d++)
 * 
 * For further references, see: 
 * 	 http://en.wikipedia.org/wiki/Multiversion_concurrency_control
 * 	 http://en.wikipedia.org/wiki/Snapshot_isolation
 * 	 http://research.microsoft.com/~simonpj/papers/stm/index.htm
 *   http://ieeexplore.ieee.org/xpl/freeabs_all.jsp?tp=&arnumber=1541121&isnumber=32906
 *   http://msdn2.microsoft.com/en-us/library/ms188277.aspx
 *   http://www.awprofessional.com/articles/article.asp?p=357098&rl=1
 *   http://en.wikipedia.org/wiki/Software_transactional_memory
 *   http://www.google.com/search?q=transactional+memory&ie=utf-8&oe=utf-8&rls=org.mozilla:en-US:official&client=firefox-a
 *   http://portal.acm.org/citation.cfm?id=165164
 *   
 * 
 * </pre>
 * 
 * @author iivanov
 * @author eduardomartins
 *  
 */
public class CacheableMap implements Map {

	
	/**
	 * the unique map key in the enclosing xa cache space
	 */
	Object mapKey = null;
		
	protected static final Object NULL_VALUE = new CacheableMap.NullCMapValue();

	/**
	 * Non public place holder class for null map values.
	 * This class will allow serialization of null values for CacheableMap.
	 * Note: It is likely that a custom externalization algorithm will be more efficient
	 * than the default serialization. To be investigated. 
	 */
	static class NullCMapValue implements Serializable {
		private static final long serialVersionUID = 1814658447351397636L;
	}
	
	/**
     * 
     * Instantiate a transaction local wrapper around an actual map 
     * 
     * @param key The unique key of the map in the enclosing cache space.
     * 
     */
    public CacheableMap(Object mapKey) {
    	if (mapKey == null) throw new NullPointerException("map key cannot be null");

    	this.mapKey = mapKey;
    }
    
	/**
	 * <pre>
	 * 
	 * Checks whether an element exists with the given key 
	 * in the data view for the current transaction. The key exists 
	 * if it exists in the txLocalMap and is not deleted, or it exists 
	 * in the actual map. 
	 * 
	 * If this method returns True, then it is guaranteed that an immediate 
	 * call to get(key) will also find an entry with the same key. Although 
	 * in this case get(key) will return null if the map entry has null value.  
	 * 
	 * </pre>
	 * 
	 *  @param key cannot be null
	 */
	public boolean containsKey(Object key) {
		if (key == null) throw new NullPointerException("key cannot be null");

    	if (isRemoved()) {
    		throw new IllegalStateException("The map has been marked for removal");
    	}

    	snapshot();
    	
		VersionedEntry ventry = innerGet(key);
		if (ventry == null || ventry.isRemoved()) {
			return false;
		} else {
			return true;
		}
    }

    /**
     * 
     * Put object with the given key in the map.
     * 
     * @param key
     * @param newValue
     * @return the previous value under the same key. null if the key was not there before or the old value was null. 
     * 
     * @see java.util.Map#put(Object, Object)
     */
    public Object put(Object key, Object newValue) {
		if (key == null) throw new NullPointerException("key cannot be null");

		if (newValue == null) {
			newValue = NULL_VALUE;
		}
		
		if (isRemoved()) {
			// the whole map has been marked for removal
			// we need to revive it
			getTxLocalMap().revive();  
		}
		
		snapshot();
		
    	VersionedEntry ventry = innerGet(key);
    	
    	if (ventry != null) {
        	// if there is an existing entry, just update it
    		Object oldValue = null;
    		if (ventry.isRemoved()) {
    			oldValue = null; 
    		} else {
    			oldValue = ventry.getValue();
    		}
    		ventry.setValue(newValue);
    		if (oldValue != null && oldValue.equals(NULL_VALUE)) {
    			oldValue = null;
    		}
    		return oldValue;
    	} else { // ventry == null
			// since there is no actual entry, 
			// we'll create a brand new one
			VersionedEntry newEntry = new VersionedEntry(key, newValue);
			getTxLocalMap().put(key, newEntry);
			
			// there is no old value, so return null
			return null;
    	}
    }

    /**
     * 
     * Returns the value of an entry.
     * The first time a lookup for a key occurs within a given transaction,
     * the value returns is the one stored in the actual map.
     * 
     * Consequent lookups within the same transaction return
     * the most recent known value in the context of the transaction.
     * 
     * In other words, once a value is looked up, it is disassociated 
     * from the actual map until the transaction completes. Changes to the 
     * actual map applied by other committing transactions will not be
     * seen in the ongoing local transaction.
     * 
     * The semantics support isolation level ANSI REPEATABLE_READ.
     * 
     */
	public Object get(Object key) {
		if (key == null) throw new NullPointerException("key cannot be null");
		
    	if (isRemoved()) {
    		throw new IllegalStateException("The map has been marked for removal");
    	}
		
		snapshot();

		VersionedEntry ventry = innerGet(key);
		if (ventry == null || ventry.isRemoved()) {
			return null;
		} else {
			Object value = ventry.getValue();
			if (NULL_VALUE.equals(value)) {
				value = null;
			}
			return value;
		}
    }

	/**
	 * When phantom reads are not allowed,
	 * Take once a snapshot of the actual map and project it to the tx local map.
	 * This is done on the first read operation to this map in the current transaction.
	 *
	 */
	private void snapshot() {
		TxLocalMap txLocalMap = getTxLocalMap();
		if (txLocalMap.isAllowPhantomReads() || txLocalMap.isSnapshotTaken()) {
			return;
		} else {
			// populate the tx local map with the current actual map view
			txLocalMap.setSnapshotTaken(true);
			Map actualMap = getActualMap();
			if (actualMap != null) {
				for(Iterator i=actualMap.keySet().iterator();i.hasNext();) {
					Object nextKey = i.next();
					VersionedEntry actualEntry = (VersionedEntry)actualMap.get(nextKey);
					// store a tx local copy of the actual entry
					if (actualEntry != null) { // it is possible that the actual entry was removed while iterating over keys
						txLocalMap.put(nextKey, new VersionedEntry(actualEntry));
					}
				}
			}
		}
	}

	/**
	 * Returns a VersionedEntry for the given key.
	 * returns null if there is no tx local nor actual entry.
	 * 
	 * The first time an entry is looked up, a new version of it 
	 * is copied to the tx local map.
	 * 
	 * @param key
	 * @return
	 */
	private VersionedEntry innerGet(Object key) {
		VersionedEntry entry = (VersionedEntry)getTxLocalMap().get(key);
		if (entry != null) { 
			return entry;
		} else if (getTxLocalMap().isAllowPhantomReads()) { // entry == null
			// nothing found in the tx local map, let's look in the actual map
			Map actualMap = getActualMap();
			if (actualMap != null) {
				VersionedEntry actualEntry = (VersionedEntry)actualMap.get(key);
				if (actualEntry == null) {
					// there is nothing under the given key in the actual map as well
					return null;
				} else {
					// store a tx local copy of the actual entry
					VersionedEntry txLocalEntry = new VersionedEntry(actualEntry);
					getTxLocalMap().put(key, txLocalEntry);
					return txLocalEntry;
				}
			}
			else {
				// actual map does not exists
				return null;
			}
		} else { // the entry is not in the local map and phantomReads are not allowed so we can no longer look in the 
			return null;
		}
	}

    /**
     * 
     * Remove object with the given key in the map.
     * 
     * @param key
     * @return the value under the key, before it was removed. null if the key was not there before or the old value was null. 
     * 
     * @see java.util.Map#put(Object, Object)
     */
    public Object remove(Object key) {
    	
    	if (isRemoved()) {
    		// the whole map is marked for removal
    		// there is nothing to be found
    		return null;
    	}
    	
    	snapshot();
    	
    	// fetch the most recent value either from the local tx cache or from the distributed cache  
    	Object oldValue = innerGet(key);
    	
    	if (oldValue == null) {
    		// there is nothing in the cache
    		return null;
    	} else {
			VersionedEntry ventry = (VersionedEntry)oldValue;
			if (ventry.isRemoved()) {
				// there is logically nothing in the cache
				return null;
			} else {
				// mark the entry for removal
				Object result = ventry.getValue();
				ventry.markRemoved();
				return result;
			}
    	}
    }

    /**
     * 
     * This is a potentially expensive operation since it may need the find the union of the tx local and dcache copy of the map.
     * 
     */
    public int size() {
    	if (isRemoved()) {
    		throw new IllegalStateException("The map has been marked for removal");
    	}
    	
    	return keySet().size();
    }

    /**
     * 
     * Returns true if size() is 0, false otherwise. 
     * Note: Potentially expensive operation, because it may involve union of tx local and actual maps.
     * 
     */
    public boolean isEmpty() {
    	boolean isEmpty = (size() == 0); 
    	return isEmpty;
    }

    /**
     * 
     * Returns a copy of the set of keys that are in sync with the tx local changes.
     * Note: This can be an expensive operation as it requires union of local changes with the actual map.
     * 
     */
    public Set keySet() {
    	
    	if (isRemoved()) {
    		throw new IllegalStateException("The map has been marked for removal");
    	}
    	
    	HashSet result = new HashSet();
    	
    	if (getTxLocalMap().isAllowPhantomReads()) {
	    	// start with the actual map key set
    		Map actualMap = getActualMap();
    		if (actualMap != null) {
    			result.addAll(actualMap.keySet());
    		}
    	} else {
    		snapshot();
    	}

    	// next, update it with the tx local changes
    	Iterator newEntries = getTxLocalMap().entrySet().iterator(); 
    	while (newEntries.hasNext()) {
    		Map.Entry nextEntry = (Map.Entry)newEntries.next();
    		VersionedEntry ventry = (VersionedEntry)nextEntry.getValue();
    		if (ventry.isRemoved()) {
    			// remove from the result keys removed in the local tx 
    			result.remove(nextEntry.getKey());
    		} else {
    			// else add the key to the result in case it hasn't been added already
    			result.add(nextEntry.getKey());
    		}
    	}
    	return result;
    }

    /**
     * 
     * Clears the tx local cache and sets a flag that clear was requested on the distributed cache.
     * When the caches are synced up at the end of the tx, the distributed cache will be cleared.
     * 
     * @see java.util.Map#clear()
     */
    public void clear() {
    	
    	if (isRemoved()) {
    		// if the whole map was marked for removal, clear() makes no difference
    		return;
    	}
    	
    	if (getTxLocalMap().isAllowPhantomReads()) {
    		Map actualMap = getActualMap();
    		if(actualMap != null) {
    			Set actualKeys = actualMap.keySet();
            	Iterator iter = actualKeys.iterator();
            	while (iter.hasNext()) {
            		Object key = iter.next();
            		// force tx local copy of each key contained in the actual map
            		// so that we can then safely mark for removal
            		innerGet(key);
            	};
    		}
    	} else {
    		snapshot();
    	}
    	
    	// mark for removal each entry 
    	Iterator localIter = getTxLocalMap().keySet().iterator();
    	while (localIter.hasNext()) {
    		remove(localIter.next());
    	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map map) {
    	if (map == null) throw new NullPointerException("map to be merged cannot be null");

    	// iterate over all map entries and put each one individually
    	//   to ensure that proper checks and update steps are applied to the multi-layer tx map structures
    	Iterator iter = map.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry entry = (Map.Entry)iter.next();
    		put(entry.getKey(), entry.getValue());
    	}
    	
    }
    
    /**
     * Not supported. The operation is too time consuming and 
     *   there haven't been good use cases for it identified. 
     */
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

    /**
     * Merges the actual data into the tx local data and returns the entrySet of the result.
     *   The operation can be time consuming. 
     *  
     */
	public Set entrySet() {
    	if (isRemoved()) {
    		throw new IllegalStateException("The map has been marked for removal");
    	}
		
		Set keys = keySet();
		Map rawEntries = new HashMap();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			Object nextKey = iter.next();
			// call get(key) to force copy of value to tx local map
			rawEntries.put(nextKey, get(nextKey));
		};
		Set entries = rawEntries.entrySet();
		return entries;
	}

    /**
     * Merges the actual data into the tx local data and returns the values of the result.
     *   The operation can be time consuming. 
     *  
     */
	public Collection values() {
		
    	if (isRemoved()) {
    		throw new IllegalStateException("The map has been marked for removal");
    	}
		
		Set keys = keySet();
		Collection rawValues = new ArrayList();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			Object nextKey = iter.next();
			// call get(key) to force copy of value to tx local map
			rawValues.add(get(nextKey));
		};
		return rawValues;
	}
	
	/**
	 * Marks the map for removal from the enclosing cache space.
	 * The actual removal is not immediate. It occurs near the point when the transaction is committed,
	 * if at all. 
	 *
	 */
	public void remove() {
		getTxLocalMap().remove(); 
	}

	/**
	 * 
	 * non-public method. Used at time near tx commit
	 * to determine whether 
	 * the map is makred for removal.  
	 * 
	 * @return
	 */
	boolean isRemoved() {
		return getTxLocalMap().isRemoved(); 
	}

	private TxLocalMap getTxLocalMap() {		
		return XACache.getInstance().getTxLocalMap(mapKey);
	}

	private Map getActualMap() {
		return XACache.getInstance().getActualMap(mapKey);
	}

	/**
	 * 
	 * When phantom reads are allowed, it is possible for code running
	 * in transaction A to see changes made in transaction B, after B committs
	 * and before A committs. 
	 * 
	 * @param boolean allowPhantomReads
	 */
	public void setAllowPhantomReads(boolean allowPhantoms) {
		getTxLocalMap().setAllowPhantomReads(allowPhantoms);
	}
	
	public boolean equals(Object obj) {
		// check all local entries have the same value
		if(obj != null && obj.getClass() == this.getClass()) {
			CacheableMap other = (CacheableMap)obj;
			TxLocalMap thisTxLocalMap = this.getTxLocalMap();
			TxLocalMap otherTxLocalMap = other.getTxLocalMap();
			if (this.mapKey.equals(other.mapKey)) {				
				if (thisTxLocalMap.isEmpty()) {
					if (otherTxLocalMap.isEmpty()) {
						// both didn't have a local map					
						return true;
					}
					else {
						// this didn't have a local map, compare with actual entries
						Map actualMap = getActualMap();
						if (actualMap != null) {
							return actualMap.entrySet().equals(otherTxLocalMap.entrySet());
						}
						else {
							//other tx local map is not empty so it doesn't equal actual map
							return false;
						}					
					}
				}
				else if (otherTxLocalMap.isEmpty()) {
					// other didn't have a local map, but this have, compare with actual map
					Map actualMap = getActualMap();
					if (actualMap != null) {
						return actualMap.entrySet().equals(thisTxLocalMap.entrySet());
					}
					else {
						//this tx local map is not empty so it doesn't equal actual map
						return false;
					}
					
				}
				else {
					// both did have local map, just compare
					return thisTxLocalMap.equals(otherTxLocalMap);
				}										
			}
			else {
				return false;
			}
		} else {
			return false;
		}
				
	}
		
	public int hashCode() {		
		return mapKey.hashCode();
	}
}

