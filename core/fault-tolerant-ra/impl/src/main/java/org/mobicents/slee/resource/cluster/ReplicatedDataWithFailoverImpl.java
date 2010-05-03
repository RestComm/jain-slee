/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import org.mobicents.cluster.MobicentsCluster;

/**
 * @author martins
 * 
 */
public class ReplicatedDataWithFailoverImpl<K extends Serializable, V extends Serializable>
		extends ReplicatedDataImpl<K, V> implements
		ReplicatedDataWithFailover<K, V> {

	/**
	 * 
	 */
	private final FailOverListener<K, V> clientLocalListener;

	/**
	 * @param name
	 * @param raEntity
	 * @param cluster
	 */
	public ReplicatedDataWithFailoverImpl(String name, String raEntity,
			MobicentsCluster cluster, FaultTolerantResourceAdaptor<K, V> ra,boolean activateDataRemovedCallback) {
		super(name, raEntity, cluster, ra, activateDataRemovedCallback);
		clientLocalListener = new FailOverListener<K, V>(ra, getCacheData());
		cluster.addFailOverListener(clientLocalListener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedDataImpl#remove()
	 */
	@Override
	public void remove() {
		getCluster().removeFailOverListener(clientLocalListener);
		super.remove();
	}
}
