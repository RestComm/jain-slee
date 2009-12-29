/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import org.jgroups.Address;

/**
 * @author martins
 *
 */
public class MemberAddressImpl implements MemberAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Address address;
	
	/**
	 * @param address
	 */
	public MemberAddressImpl(Address address) {
		if (address == null) {
			throw new NullPointerException("null address");
		}
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return address.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MemberAddressImpl)obj).address.equals(this.address);
		}
		else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(MemberAddress)
	 */
	@SuppressWarnings("unchecked")
	public int compareTo(MemberAddress o) {
		return address.compareTo(((MemberAddressImpl)o).address);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return address.toString();
	}
	
}
