package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.ErrorAddress;

/**
 * 
 * @author amit bhayani
 *
 */
public class ErrorAddressImpl extends AddressImpl implements ErrorAddress {

	private int error;

	public ErrorAddressImpl(int ton, int npi, String address, int error) {
		super(ton, npi, address);
	}

	public int getError() {
		return this.error;
	}

	public void setError(int error) {
		this.error = error;
	}

}
