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
public interface FaultTolerantResourceAdaptorContext<K extends SerializableActivityHandle, V extends Serializable> {

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
	 * @return
	 */
	public ReplicatedData<K, V> getReplicateData();

	/**
	 * Retrieves the {@link ReplicatedDataWithFailover} for the
	 * {@link FaultTolerantResourceAdaptor}
	 * 
	 * @return
	 */
	public ReplicatedDataWithFailover<K, V> getReplicatedDataWithFailover();

}
