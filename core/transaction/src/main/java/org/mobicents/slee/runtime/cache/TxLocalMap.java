package org.mobicents.slee.runtime.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * Helper class used to store tx local map data 
 * 
 * @author Ivelin Ivanov
 *
 */
class TxLocalMap extends ConcurrentHashMap {
	
	/**
	 * not intended to be serialized!
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * designates whether the map instance has been marked for removal
	 * in the context of a transaction. The actual removal occurs near 
	 * the point when the transaction commits, if it does.
	 */
	private boolean isRemoved = false;
	
	/**
	 * indicates that in this tx context this map exists in cache
	 */
	private boolean inCache = false;
	
	/**
	 * used when phantom reads == false. 
	 * Flags wheter a full snapshot of the actual map has been taken 
	 */
	private boolean isSnapshotTaken = false;

	/**
	 * Flags whether or not phantom reads are allowed for the current map
	 */
	private boolean isAllowPhantomReads = true;
	
	TxLocalMap(boolean inCache) {
		super();
		this.inCache = inCache;
	}
	
	void remove() {
		isRemoved = true;
	}

	boolean isInCache() {
		return inCache;
	}
	
	boolean isRemoved() {
		return isRemoved;
	}
	
	/**
	 * 
	 * used when a tx local map is removed, but then used again before tx ends  
	 *
	 */
	void revive() {
		clear();
		isRemoved = false;
	}

	public boolean isSnapshotTaken() {
		return isSnapshotTaken ;
	}
	
	public void setSnapshotTaken(boolean b) {
		isSnapshotTaken = b;
	}
	

	public boolean isAllowPhantomReads() {
		return isAllowPhantomReads;
	}
	
	public void setAllowPhantomReads(boolean b) {
		isAllowPhantomReads = b;
	}
		
	public boolean equals(Object obj) {		
		if(obj != null && obj.getClass() == this.getClass()) {
			TxLocalMap other = (TxLocalMap) obj;
			return this.isRemoved == other.isRemoved && this.entrySet().equals(other.entrySet());
		}
		else {
			return false;		
		}
	}
	
	@Override
	public int hashCode() {
		return this.entrySet().hashCode()*31+(isRemoved()?0:1);
	}
	
}
