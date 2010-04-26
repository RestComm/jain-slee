package org.mobicents.slee.container.service;

import java.io.Serializable;

import javax.slee.ServiceID;

import org.mobicents.slee.container.activity.ActivityContextHandle;

public class ServiceActivityHandleImpl implements ServiceActivityHandle, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ServiceID serviceid;

	public ServiceActivityHandleImpl(ServiceID serviceid) {
		this.serviceid = serviceid;
	}
    
	public boolean equals(Object obj) {
		if ((obj != null) && (obj.getClass() == this.getClass())) {
			return this.serviceid.equals(((ServiceActivityHandleImpl) obj).serviceid);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.service.ServiceActivityHandle#getActivityContextHandle()
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return new ServiceActivityContextHandle(this);
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
