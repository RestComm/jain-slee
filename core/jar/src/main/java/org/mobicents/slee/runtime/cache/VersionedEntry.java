package org.mobicents.slee.runtime.cache;

/**
 * 
 * Encapsulates a single entry in a transactional cache map.
 * It has convenience methods to support optimistick locking with MVCC. 
 * 
 * Note: This class is not thread safe.
 * 
 * @author Ivelin Ivanov
 *
 */
public class VersionedEntry {
	
	private Object key = null;
	private Object value = null;
	
	// flags whether the entry has been modified or removed since it was instantiated
	private boolean isDirty = false;
	
	private long version = 0;
	
	// designates whether an entry is marked for removal or it holds a value
	private boolean isRemoved = false;
	
	private VersionedEntry() {
		// prohibits creating instances without explicit key
	}
	
	/**
	 * Creates a new entry based on the state of an original entry. 
	 * The newly instantiated entry assumes a newer version than the original.
	 * 
	 * @param key
	 * @param val
	 * @param currentVersion
	 */
	public VersionedEntry(VersionedEntry originalEntry) {
		this.key = originalEntry.key;
		this.value = originalEntry.value;
		this.isRemoved = originalEntry.isRemoved;
		this.version = originalEntry.version+1;
	}
	
	/**
	 * 
	 * Creates a new entry with the given key and value, and version = 0
	 * 
	 * @param key
	 * @param val
	 * @param currentVersion
	 */
	public VersionedEntry(Object key, Object val) {
		this.key = key;
		this.value = val;
		this.isDirty = true;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		if (isRemoved) throw new IllegalStateException("cannot get value for a removed entry");
		return value;
	}
	
	public void setValue(Object val) {
		isDirty = true;
		isRemoved = false;
		value = val;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void markRemoved() {
		isDirty = true;
		isRemoved = true; 
	}
	
	public boolean isRemoved() {
		return isRemoved;
	}
	
	public boolean isNewerVersion(VersionedEntry other) {
		return version > other.getVersion();
	}
	
	public boolean isDirty() {
		return isDirty;
	}
	
	public String toString() {
		String result = "VersionedEntry: {" +
				"\n  key       : " + key +
				"\n  version   : " + version +
				"\n  value     : " + value +
				"\n  isRemoved : " + isRemoved +
				"\n}";
		return result;
	}
	
}
