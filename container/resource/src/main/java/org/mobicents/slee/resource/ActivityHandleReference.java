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

package org.mobicents.slee.resource;

import javax.slee.resource.ActivityHandle;

import org.jgroups.Address;

/**
 * A reference activity handle, which is used instead of the real handle when this one is not replicated.
 * @author martins
 *
 */
public class ActivityHandleReference implements ActivityHandle {
	
	private ActivityHandle reference;
	private final String id;
	private final Address address;
	
	public ActivityHandleReference(ActivityHandle reference, Address address, String id) {
		this.reference = reference;
		this.address = address;
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}
	
	public ActivityHandle getReference() {
		return reference;
	}

	public void setReference(ActivityHandle reference) {
		this.reference = reference;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode()*31+address.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ActivityHandleReference other = (ActivityHandleReference) obj;
		return this.id.equals(other.id) && this.address.equals(other.address);
	}
	
	@Override
	public String toString() {
		return new StringBuilder(address.toString()).append(':').append(id).toString();
	}
}
