package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;

/**
 * 
 * @author amit bhayani
 *
 */
public class AddressImpl implements Address {

	private String address;
	private int npi;
	private int ton;

	public AddressImpl(int ton, int npi, String address) {
		this.address = address;
		this.ton = ton;
		this.npi = npi;
	}

	public String getAddress() {
		return this.address;
	}

	public int getAddressNpi() {
		return this.npi;
	}

	public int getAddressTon() {
		return this.ton;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + npi;
		result = prime * result + ton;
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
		final AddressImpl other = (AddressImpl) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (npi != other.npi)
			return false;
		if (ton != other.ton)
			return false;
		return true;
	}
	
	
	
	
}
