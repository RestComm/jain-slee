package org.mobicents.slee.resource.media.ra;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

public class MsConnectionActivityHandle implements Serializable, ActivityHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5484151092577390208L;
	
	private final String id;
	
	public MsConnectionActivityHandle(String id){
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MsConnectionActivityHandle)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "MsConnectionActivityHandle(id="+id+")";
	}

}
