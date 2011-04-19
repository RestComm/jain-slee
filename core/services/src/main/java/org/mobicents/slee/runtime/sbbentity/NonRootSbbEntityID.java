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

public class NonRootSbbEntityID implements SbbEntityID {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SbbEntityID parentSbbEntityID;
	private String parentChildRelation;
	private String childID;
	
	/**
	 * not to be used, needed due to externalizable
	 */
	public NonRootSbbEntityID() {
		
	}
	
	public NonRootSbbEntityID(SbbEntityID parentSbbEntityID, String parentChildRelation, String childID) {
		this.parentSbbEntityID = parentSbbEntityID;
		this.parentChildRelation = parentChildRelation;
		this.childID = childID;
	}
	
	public String getChildID() {
		return childID;
	}
	
	@Override
	public String getName() {
		return childID;
	}
	
	@Override
	public SbbEntityID getParentSBBEntityID() {
		return parentSbbEntityID;
	}

	@Override
	public String getParentChildRelation() {
		return parentChildRelation;
	}

	@Override
	public ServiceID getServiceID() {
		return parentSbbEntityID.getServiceID();
	}

	@Override
	public String getServiceConvergenceName() {
		return parentSbbEntityID.getServiceConvergenceName();
	}

	@Override
	public boolean isRootSbbEntity() {
		return false;
	}

	@Override
	public SbbEntityID getRootSBBEntityID() {
		return parentSbbEntityID.getRootSBBEntityID();
	}
	
	@Override
	public int hashCode() {		
		int result = childID.hashCode();
		result = result*31 + parentChildRelation.hashCode();
		result = result*31 + parentSbbEntityID.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NonRootSbbEntityID other = (NonRootSbbEntityID) obj;
		if (!parentSbbEntityID.equals(other.parentSbbEntityID))
			return false;
		if (!parentChildRelation.equals(other.parentChildRelation))
			return false;
		if (!childID.equals(other.childID))
			return false;
		return true;
	}

	private String toString = null;
	
	@Override
	public String toString() {
		if (toString == null) {
			toString = new StringBuilder(parentSbbEntityID.toString()).append("/").append(parentChildRelation).append("/").append(childID).toString();
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
		parentSbbEntityID = (SbbEntityID) in.readObject();
		parentChildRelation = in.readUTF();
		childID = in.readUTF();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(parentSbbEntityID);
		out.writeUTF(parentChildRelation);
		out.writeUTF(childID);
	}
}
