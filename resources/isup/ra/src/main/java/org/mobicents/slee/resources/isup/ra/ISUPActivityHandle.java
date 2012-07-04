/**
 * 
 */
package org.mobicents.slee.resources.isup.ra;

import javax.slee.resource.ActivityHandle;

/**
 * @author baranowb
 *
 */
public class ISUPActivityHandle implements ActivityHandle {

	private long key;

	ISUPActivityHandle(long key) {
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
		result = (int)(prime * result + key);
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
		if (key !=other.key)
			return false;
		
		return true;
	}

	

}
