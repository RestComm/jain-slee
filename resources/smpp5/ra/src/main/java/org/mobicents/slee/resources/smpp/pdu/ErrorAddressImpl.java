package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.ErrorAddress;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ErrorAddressImpl extends AddressImpl implements ErrorAddress {

	public ErrorAddressImpl(org.mobicents.protocols.smpp.ErrorAddress protoErrorAddress) {
		super(protoErrorAddress);
	}

	public ErrorAddressImpl(int ton, int npi, String address, int error) {
		this.protoAddress = new org.mobicents.protocols.smpp.ErrorAddress(ton, npi, address, error);
	}

	public int getError() {
		return (int) ((org.mobicents.protocols.smpp.ErrorAddress) this.protoAddress).getError();
	}

	public void setError(int error) {
		((org.mobicents.protocols.smpp.ErrorAddress) this.protoAddress).setError(error);
	}

}
