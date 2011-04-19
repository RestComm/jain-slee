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

/**
 * 
 */
package org.mobicents.slee.resource;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.Marshaler;

import org.jgroups.Address;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * @author martins
 *
 */
public class ResourceAdaptorActivityContextHandleImpl implements ResourceAdaptorActivityContextHandle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ActivityHandle activityHandle;
	private ResourceAdaptorEntity raEntity;
	
	/**
	 * not to be used, needed due to externalizable
	 */
	public ResourceAdaptorActivityContextHandleImpl() {

	}
	
	/**
	 * 
	 */
	public ResourceAdaptorActivityContextHandleImpl(ResourceAdaptorEntity raEntity, ActivityHandle activityHandle) {
		this.raEntity = raEntity;
		this.activityHandle = activityHandle;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityObject()
	 */
	public Object getActivityObject() {
		return raEntity.getResourceAdaptorObject().getActivity(activityHandle);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle()
	 */
	public ActivityHandle getActivityHandle() {
		return activityHandle;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptorActivityContextHandle#getResourceAdaptorEntity()
	 */
	public ResourceAdaptorEntity getResourceAdaptorEntity() {
		return raEntity;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType()
	 */
	public ActivityType getActivityType() {
		return ActivityType.RA;
	}

	/*
	 * (non-Javadoc)
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
			final ResourceAdaptorActivityContextHandleImpl other = (ResourceAdaptorActivityContextHandleImpl) obj;
			return other.activityHandle.equals(this.activityHandle) && other.raEntity.equals(this.raEntity);
		} else {
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return activityHandle.hashCode() * 31 + raEntity.hashCode();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder ("RA:").append(getResourceAdaptorEntity().getName()).append(':').append(activityHandle).toString(); 		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// read ra entity name
		final String raEntityName = in.readUTF();
		// read activity handle
		this.raEntity = SleeContainer.lookupFromJndi().getResourceManagement()
		.getResourceAdaptorEntity(raEntityName);
		if (raEntity == null) {
			throw new IOException("RA Entity with name " + raEntityName
					+ " not found.");
		}
		// read activity handle
		boolean handleReference = in.readBoolean();
		if (handleReference) {
			// a reference
			activityHandle = new ActivityHandleReference(null, (Address) in.readObject(), in.readUTF());
		} else {
			final Marshaler marshaler = raEntity.getMarshaler();
			if (marshaler != null) {
				activityHandle = marshaler.unmarshalHandle(in);
			} else {
				throw new IOException("marshaller from RA is null");
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// write ra entity name
		out.writeUTF(raEntity.getName());
		// write activity handle
		if (activityHandle.getClass() == ActivityHandleReference.class) {
			// a reference
			out.writeBoolean(true);
			final ActivityHandleReference reference = (ActivityHandleReference) activityHandle;
			out.writeObject(reference.getAddress());
			out.writeUTF(reference.getId());
		}
		else {
			out.writeBoolean(false);
			final Marshaler marshaler = raEntity.getMarshaler();
			if (marshaler != null) {
				marshaler.marshalHandle(activityHandle, out);
			}
			else {
				throw new IOException("marshaller from RA is null");
			}
		}
	}
	
}
