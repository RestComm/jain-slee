package org.mobicents.slee.container.service;

import java.io.Serializable;

import javax.slee.ServiceID;
import javax.slee.resource.ActivityHandle;

public class ServiceActivityHandle implements ActivityHandle, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ServiceID serviceid;

	public ServiceActivityHandle(ServiceID serviceid) {
		this.serviceid = serviceid;
	}
    
	public boolean equals(Object obj) {
		if ((obj != null) && (obj.getClass() == this.getClass())) {
			return this.serviceid.equals(((ServiceActivityHandle) obj).serviceid);
		} else {
			return false;
		}
	}

	public ServiceID getServiceID() {
		return serviceid;
	}
	
	public int hashCode() {
		return serviceid.hashCode();
	}
    
	@Override
	public String toString() {
		return serviceid.toString();
	}
}
