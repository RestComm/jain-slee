package org.mobicents.slee.resource.tftp;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

/**
 * Base class for an activity handle
 */
public class TransferActivityHandle implements ActivityHandle, Serializable {

	private static final long serialVersionUID = 1L;

	protected final String id;

	public TransferActivityHandle(String id) {
		if (id == null)
			throw new NullPointerException("null id");

		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((TransferActivityHandle)obj).id.equals(this.id);
		} else {
			return false;
		}
	}
}
