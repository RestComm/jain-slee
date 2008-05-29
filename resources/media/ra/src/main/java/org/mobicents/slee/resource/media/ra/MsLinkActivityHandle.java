package org.mobicents.slee.resource.media.ra;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

public class MsLinkActivityHandle implements Serializable, ActivityHandle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5873066577664979438L;
	private final String id;
	
	public MsLinkActivityHandle(String id){
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MsLinkActivityHandle)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "MsLinkActivityHandle(id="+id+")";
	}

}
