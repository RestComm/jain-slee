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

/**
 * 
 */
package org.mobicents.slee.container.service;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.slee.ServiceID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;

/**
 * @author martins
 * 
 */
public class ServiceActivityContextHandle implements ActivityContextHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ServiceActivityHandleImpl activityHandle;

	/**
	 * not to be used, needed due to externalizable
	 */
	public ServiceActivityContextHandle() {

	}

	/**
	 * 
	 */
	public ServiceActivityContextHandle(ServiceActivityHandleImpl activityHandle) {
		this.activityHandle = activityHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivity()
	 */
	public Object getActivityObject() {
		return new ServiceActivityImpl(activityHandle.getServiceID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle
	 * ()
	 */
	public ServiceActivityHandleImpl getActivityHandle() {
		return activityHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType
	 * ()
	 */
	public ActivityType getActivityType() {
		return ActivityType.SERVICE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj.getClass() == this.getClass()) {
			final ServiceActivityContextHandle other = (ServiceActivityContextHandle) obj;
			return other.activityHandle.equals(this.activityHandle);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return activityHandle.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder("ACH=").append(getActivityType()).append('>')
				.append(activityHandle).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		final ServiceID serviceID = new ServiceID(in.readUTF(), in.readUTF(),
				in.readUTF());
		activityHandle = new ServiceActivityHandleImpl(serviceID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		final ServiceID serviceID = activityHandle.getServiceID();
		out.writeUTF(serviceID.getName());
		out.writeUTF(serviceID.getVendor());
		out.writeUTF(serviceID.getVersion());
	}
}
