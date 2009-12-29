/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import org.jboss.cache.Fqn;
import org.jgroups.Address;
import org.mobicents.cluster.cache.ClusteredCacheData;

/**
 * @author martins
 * 
 */
public class ClientLocalListener<K extends Serializable, V extends Serializable>
		implements org.mobicents.cluster.ClientLocalListener {

	private final FaultTolerantResourceAdaptor<K, V> ra;
	private final ReplicatedDataCacheData<K, V> baseCacheData;

	/**
	 * @param ra
	 * @param baseFqn
	 */
	public ClientLocalListener(FaultTolerantResourceAdaptor<K, V> ra,
			ReplicatedDataCacheData<K, V> baseCacheData) {
		this.ra = ra;
		this.baseCacheData = baseCacheData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.cluster.ClientLocalListener#failOverClusterMember(org.jgroups
	 * .Address)
	 */
	public void failOverClusterMember(Address address) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.cluster.ClientLocalListener#getBaseFqn()
	 */
	@SuppressWarnings("unchecked")
	public Fqn getBaseFqn() {
		return baseCacheData.getNodeFqn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.cluster.ClientLocalListener#getPriority()
	 */
	public byte getPriority() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.cluster.ClientLocalListener#lostOwnership(org.mobicents
	 * .cluster.cache.ClusteredCacheData)
	 */
	public void lostOwnership(ClusteredCacheData clusteredCacheData) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.cluster.ClientLocalListener#wonOwnership(org.mobicents.
	 * cluster.cache.ClusteredCacheData)
	 */
	@SuppressWarnings("unchecked")
	public void wonOwnership(ClusteredCacheData clusteredCacheData) {
		ra.failOver((K) clusteredCacheData.getNodeFqn().getLastElement());
	}

}
