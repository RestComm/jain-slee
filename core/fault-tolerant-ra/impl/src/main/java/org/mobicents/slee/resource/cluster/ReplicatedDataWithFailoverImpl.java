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
	private final ClientLocalListener<K, V> clientLocalListener;

	/**
	 * @param name
	 * @param raEntity
	 * @param cluster
	 */
	public ReplicatedDataWithFailoverImpl(String name, String raEntity,
			MobicentsCluster cluster, FaultTolerantResourceAdaptor<K, V> ra) {
		super(name, raEntity, cluster);
		clientLocalListener = new ClientLocalListener<K, V>(ra, getCacheData());
		cluster.addLocalListener(clientLocalListener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.cluster.ReplicatedDataImpl#remove()
	 */
	@Override
	public void remove() {
		getCluster().removeLocalListener(clientLocalListener);
		super.remove();
	}
}
