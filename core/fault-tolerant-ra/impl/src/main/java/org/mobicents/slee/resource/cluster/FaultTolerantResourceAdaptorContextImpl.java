/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import org.jgroups.Address;
import org.mobicents.cluster.MobicentsCluster;

/**
 * Implementation of the fault tolerant ra context.
 * 
 * @author martins
 * 
 */
public class FaultTolerantResourceAdaptorContextImpl<K extends Serializable, V extends Serializable>
		implements FaultTolerantResourceAdaptorContext<K, V> {

	private static final String REPLICATED_DATA_WITH_FAILOVER_NAME = "ra-data-fo";
	private static final String REPLICATED_DATA_NAME = "ra-data";

	private ReplicatedDataWithFailoverImpl<K, V> replicatedDataWithFailover = null;
	private ReplicatedDataImpl<K, V> replicatedData = null;

	private final String raEntity;
	private final MobicentsCluster cluster;
	private final FaultTolerantResourceAdaptor<K, V> ra;

	/**
	 * @param raEntity
	 * @param cluster
	 * @param ra
	 */
	public FaultTolerantResourceAdaptorContextImpl(String raEntity,
			MobicentsCluster cluster, FaultTolerantResourceAdaptor<K, V> ra) {
		this.raEntity = raEntity;
		this.cluster = cluster;
		this.ra = ra;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext#getReplicatedDataWithFailover()
	 */
	public ReplicatedDataWithFailover<K, V> getReplicatedDataWithFailover() {
		if (replicatedDataWithFailover == null) {
			replicatedDataWithFailover = new ReplicatedDataWithFailoverImpl<K, V>(
					REPLICATED_DATA_WITH_FAILOVER_NAME, raEntity, cluster, ra);
		}
		return replicatedDataWithFailover;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext#getReplicateData()
	 */
	public ReplicatedData<K, V> getReplicateData() {
		if (replicatedData == null) {
			replicatedData = new ReplicatedDataImpl<K, V>(REPLICATED_DATA_NAME,
					raEntity, cluster);
		}
		return replicatedData;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext#isLocal()
	 */
	public boolean isLocal() {
		return cluster.getMobicentsCache().isLocalMode();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext#isHeadMember()
	 */
	public boolean isHeadMember() {
		return cluster.isHeadMember();
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext#isSingleMember()
	 */
	public boolean isSingleMember() {
		return cluster.isSingleMember();
	}
	
	private static final Address[] EMPTY_ADDRESS_ARRAY = {};
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext#getMembers()
	 */
	public MemberAddress[] getMembers() {
		final Address[] addresses = cluster.getClusterMembers().toArray(EMPTY_ADDRESS_ARRAY);
		final MemberAddressImpl[] members = new MemberAddressImpl[addresses.length];
		for (int i = 0;i<members.length;i++) {
			members[i] = new MemberAddressImpl(addresses[i]);
		}
		return members;
	}
	
	/**
	 * Removes all replicated data
	 */
	public void removeReplicateData() {
		if (replicatedDataWithFailover != null) {
			replicatedDataWithFailover.remove();
			replicatedDataWithFailover = null;
		}
		if (replicatedData != null) {
			replicatedData.remove();
			replicatedData = null;
		}
	}
}
