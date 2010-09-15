package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AddressImpl implements Address {

	protected org.mobicents.protocols.smpp.Address protoAddress;
	
	protected AddressImpl(){
	}
	
	public AddressImpl(org.mobicents.protocols.smpp.Address protoAddress){
		this.protoAddress = protoAddress;
	}

	public AddressImpl(int ton, int npi, String address) {
		protoAddress = new org.mobicents.protocols.smpp.Address(ton, npi, address);
	}

	public String getAddress() {
		return this.protoAddress.getAddress();
	}

	public int getAddressNpi() {
		return this.protoAddress.getNPI();
	}

	public int getAddressTon() {
		return this.protoAddress.getTON();
	}

	@Override
	public int hashCode() {
		return this.protoAddress.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.protoAddress.equals(obj);
	}

	public org.mobicents.protocols.smpp.Address getProtoAddress() {
		return this.protoAddress;
	}

}
