/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.Set;

import javax.slee.resource.ActivityHandle;

/**
 * A container for serializable activity handles, which are replicated in
 * the SLEE cluster, but don't require any failover.
 * 
 * @author martins
 * 
 */
public interface ReplicatedData<K extends Serializable & ActivityHandle, V extends Serializable> {

	/**
	 * Retrieves a set with data belonging to the cluster member.
	 * 
	 * Note that this is a VERY expensive operation, with respect to performance.
	 * 
	 * @return
	 */
	public Set<K> getLocalHandles();

	/**
	 * Put the handle, with the specified activity (may be null for Set like
	 * usage) into replicate data.
	 * 
	 * @param handle
	 * @param activity
	 * @return
	 */
	public boolean put(K handle, V activity);

	/**
	 * Retrieves the activity stored along the specified handle. Note that this
	 * method may return null but the handle exist (follow up of a put(K,null)).
	 * 
	 * @param handle
	 * @return
	 */
	public V get(K handle);

	/**
	 * Tests if the specified handle is in replicated data.
	 * 
	 * @param handle
	 * @return
	 */
	public boolean contains(K handle);

	/**
	 * Removes the specified handle from replicated data.
	 * @param handle
	 * @return true if the node existed and was removed
	 */
	public boolean remove(K handle);

	/**
	 * Retrieves all handles being replicated, including the ones "belonging" to
	 * other cluster nodes.
	 * 
	 * @return
	 */
	public Set<K> getReplicatedHandles();

}
