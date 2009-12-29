/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.Set;

/**
 * A container for serializable data, which are replicated in
 * the SLEE cluster, but don't require any failover.
 * 
 * @author martins
 * 
 */
public interface ReplicatedData<K extends Serializable, V extends Serializable> {

	/**
	 * Retrieves a set with data belonging to the cluster member.
	 * 
	 * Note that this is a VERY expensive operation, with respect to performance.
	 * 
	 * @return
	 */
	public Set<K> getLocalKeyset();

	/**
	 * Put the key, with the specified value (may be null for Set like
	 * usage) into replicate data.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(K key, V value);

	/**
	 * Retrieves the value stored for the specified key. Note that this
	 * method may return null but the key exist (follow up of a put(K,null)).
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * Tests if the specified key is in replicated data.
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(K key);

	/**
	 * Removes the specified key from replicated data.
	 * @param key
	 * @return true if the node existed and was removed
	 */
	public boolean remove(K key);

	/**
	 * Retrieves all key being replicated, including the ones "belonging" to
	 * other cluster nodes.
	 * 
	 * @return
	 */
	public Set<K> getKeyset();

}
