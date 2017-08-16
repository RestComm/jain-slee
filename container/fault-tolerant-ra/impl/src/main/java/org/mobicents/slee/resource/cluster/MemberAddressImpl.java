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
package org.mobicents.slee.resource.cluster;

import org.infinispan.remoting.transport.Address;

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
