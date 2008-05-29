package org.mobicents.slee.resource.media.ra;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

public class MsResourceActivityHandle implements Serializable, ActivityHandle {



	/**
	 * 
	 */
	private static final long serialVersionUID = -5005205737504399572L;
	private final String id;
	
	public MsResourceActivityHandle(String id){
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MsResourceActivityHandle)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "MsResourceActivityHandle(id="+id+")";
	}

}
