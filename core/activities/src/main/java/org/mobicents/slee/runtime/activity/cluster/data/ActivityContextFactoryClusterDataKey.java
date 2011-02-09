package org.mobicents.slee.runtime.activity.cluster.data;

import org.mobicents.cluster.data.ClusterDataKey;

public class ActivityContextFactoryClusterDataKey implements ClusterDataKey {

	private final String name;
	
	public ActivityContextFactoryClusterDataKey(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityContextFactoryClusterDataKey other = (ActivityContextFactoryClusterDataKey) obj;
		return name.equals(other.name);
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
