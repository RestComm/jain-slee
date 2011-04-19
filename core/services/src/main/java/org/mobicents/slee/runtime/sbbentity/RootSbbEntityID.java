/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.runtime.sbbentity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.slee.ServiceID;

import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * 
 * @author martins
 * 
 */
public class RootSbbEntityID implements SbbEntityID {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ServiceID serviceID;
	private String convergenceName;

	private String toString = null;

	/**
	 * not to be used, needed due to externalizable
	 */
	public RootSbbEntityID() {

	}

	/**
	 * 
	 * @param serviceID
	 * @param convergenceName
	 */
	public RootSbbEntityID(ServiceID serviceID, String convergenceName) {
		this.serviceID = serviceID;
		this.convergenceName = convergenceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.sbbentity.SbbEntityID#getParentSBBEntityID()
	 */
	@Override
	public SbbEntityID getParentSBBEntityID() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.sbbentity.SbbEntityID#getParentChildRelation
	 * ()
	 */
	@Override
	public String getParentChildRelation() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.sbbentity.SbbEntityID#getServiceID()
	 */
	@Override
	public ServiceID getServiceID() {
		return serviceID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.sbbentity.SbbEntityID#getServiceConvergenceName
	 * ()
	 */
	@Override
	public String getServiceConvergenceName() {
		return convergenceName;
	}

	@Override
	public String getName() {
		return convergenceName;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.sbbentity.SbbEntityID#isRootSbbEntity()
	 */
	@Override
	public boolean isRootSbbEntity() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.sbbentity.SbbEntityID#getRootSBBEntityID()
	 */
	@Override
	public SbbEntityID getRootSBBEntityID() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = convergenceName.hashCode();
		result = result * 31 + serviceID.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RootSbbEntityID other = (RootSbbEntityID) obj;
		if (!convergenceName.equals(other.convergenceName))
			return false;
		if (!serviceID.equals(other.serviceID))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (toString == null) {
			toString = new StringBuilder("/").append(serviceID.toString())
					.append("/").append(convergenceName).toString();
		}
		return toString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		serviceID = new ServiceID(in.readUTF(), in.readUTF(), in.readUTF());
		convergenceName = in.readUTF();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(serviceID.getName());
		out.writeUTF(serviceID.getVendor());
		out.writeUTF(serviceID.getVersion());
		out.writeUTF(convergenceName);
	}

}
