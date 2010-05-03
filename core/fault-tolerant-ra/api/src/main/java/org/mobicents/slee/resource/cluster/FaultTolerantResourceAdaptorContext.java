/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

/**
 * Fault tolerant context for a {@link FaultTolerantResourceAdaptor}, gives
 * access to replicated data and information of the SLEE cluster.
 * 
 * @author martins
 * 
 */
public interface FaultTolerantResourceAdaptorContext<K extends Serializable, V extends Serializable> {

	/**
	 * Retrieves the members of the cluster.
	 * @return
	 */
	public MemberAddress[] getMembers();
	
	/**
	 * Indicates if it is the head member of the cluster.
	 * @return true if isLocal() or the node is the first member of the cluster.
	 */
	public boolean isHeadMember(); 
	
	/**
	 * Indicates if the node is the single member of the cluster.
	 * @return true if isLocal() or the cluster has a single member.
	 */
	public boolean isSingleMember();
	
	/**
	 * Indicates if the resource adaptor object is running in a local or cluster
	 * config.
	 * 
	 * @return
	 */
	public boolean isLocal();
	
	/**
	 * Retrieves the {@link ReplicatedData} for the
	 * {@link FaultTolerantResourceAdaptor}
	 * 
	 * @param activateDataRemovedCallback
	 * @return
	 */
	public ReplicatedData<K, V> getReplicateData(boolean activateDataRemovedCallback);

	/**
	 * Retrieves the {@link ReplicatedDataWithFailover} for the
	 * {@link FaultTolerantResourceAdaptor}
	 * 
	 * @param activateDataRemovedCallback
	 * @return
	 */
	public ReplicatedDataWithFailover<K, V> getReplicatedDataWithFailover(boolean activateDataRemovedCallback);

}
