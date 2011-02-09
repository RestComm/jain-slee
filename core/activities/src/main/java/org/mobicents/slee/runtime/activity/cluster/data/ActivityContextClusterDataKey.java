package org.mobicents.slee.runtime.activity.cluster.data;

import org.mobicents.cluster.data.ClusterDataKey;

public class ActivityContextClusterDataKey implements ClusterDataKey {

	private final String factoryName;
	
	public ActivityContextClusterDataKey(String factoryName) {
		super();
		this.factoryName = factoryName;
	}

	public String getName() {
		return factoryName;
	}
	
	@Override
	public int hashCode() {
		return factoryName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityContextClusterDataKey other = (ActivityContextClusterDataKey) obj;
		return factoryName.equals(other.factoryName);
	}

	@Override
	public ClusterDataKey getListenerKey() {
		return null;
	}

	@Override
	public boolean isFailedOver() {
		return false;
	}

	@Override
	public boolean storesData() {
		return false;
	}

	@Override
	public boolean usesReferences() {
		return true;
	}

}
