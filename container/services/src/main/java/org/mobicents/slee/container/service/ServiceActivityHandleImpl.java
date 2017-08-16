/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
