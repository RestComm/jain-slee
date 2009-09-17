package org.mobicents.slee.resource.mgcp.ra;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

public class MgcpEndpointActivityHandle implements Serializable, ActivityHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7278033137580542885L;
	
	private final String id;
	
	public MgcpEndpointActivityHandle(String id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MgcpEndpointActivityHandle)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "MgcpEndpointActivityHandle(id="+id+")";
	}
}
