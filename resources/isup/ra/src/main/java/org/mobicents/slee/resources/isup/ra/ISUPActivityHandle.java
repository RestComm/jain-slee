/**
 * 
 */
package org.mobicents.slee.resources.isup.ra;

import javax.slee.resource.ActivityHandle;

import org.mobicents.protocols.ss7.isup.TransactionKey;

/**
 * @author baranowb
 *
 */
public class ISUPActivityHandle implements ActivityHandle {

	private TransactionKey key;

	ISUPActivityHandle(TransactionKey key) {
		super();
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		ISUPActivityHandle other = (ISUPActivityHandle) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	

}
