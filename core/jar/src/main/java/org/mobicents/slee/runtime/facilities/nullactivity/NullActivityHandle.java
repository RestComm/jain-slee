package org.mobicents.slee.runtime.facilities.nullactivity;

import java.io.Serializable;

import javax.slee.connection.ExternalActivityHandle;
import javax.slee.resource.ActivityHandle;

/**
 * Handle for a null activity.
 * 
 * @author martins
 *
 */
public class NullActivityHandle implements ExternalActivityHandle, ActivityHandle {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String id;

    public NullActivityHandle(String id) {
		this.id = id;
	}
    
	public boolean equals(Object obj) {
		if ((obj != null) && (obj.getClass() == this.getClass())) {
			return this.id.equals(((NullActivityHandle) obj).id);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return id.hashCode();
	}
    
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {		
		return id;
	}
}
