package org.mobicents.slee.resource;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.jgroups.Address;

/**
 * A reference activity handle, which is used instead of the real handle when this one is not replicated.
 * @author martins
 *
 */
public class ActivityHandleReference implements ActivityHandle, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final transient ActivityHandle reference;
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
