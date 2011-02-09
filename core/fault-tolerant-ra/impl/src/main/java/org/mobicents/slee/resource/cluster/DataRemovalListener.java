package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import org.jboss.cache.Fqn;

public class DataRemovalListener<K extends Serializable, V extends Serializable> implements org.mobicents.cluster.DataRemovalListener {

	private final FaultTolerantResourceAdaptor<K, V> ra;
	private final ReplicatedDataCacheData<K, V> baseCacheData;

	/**
	 * @param ra
	 * @param baseFqn
	 */
	public DataRemovalListener(FaultTolerantResourceAdaptor<K, V> ra,
			ReplicatedDataCacheData<K, V> baseCacheData) {
		this.ra = ra;
		this.baseCacheData = baseCacheData;
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
	 * @see org.mobicents.cluster.DataRemovalListener#dataRemoved(org.jboss.cache.Fqn)
	 */
	@SuppressWarnings("unchecked")
	public void dataRemoved(Fqn fqn) {
		ra.dataRemoved((K)fqn.getLastElement());
	}

}
