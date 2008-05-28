package org.mobicents.slee.resource.mgcp.ra;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

public class MgcpConnectionActivityHandle implements Serializable, ActivityHandle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 813095960338759206L;
	
	private final String id;
	
	public MgcpConnectionActivityHandle(String id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MgcpConnectionActivityHandle)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "MgcpConnectionActivityHandle(id="+id+")";
	}
}
