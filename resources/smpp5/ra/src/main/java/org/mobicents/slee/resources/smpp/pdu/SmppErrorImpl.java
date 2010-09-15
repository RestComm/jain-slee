package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.PDU;
import net.java.slee.resources.smpp.pdu.SmppError;

/**
 * 
 * @author amit bhayani
 *
 */
public class SmppErrorImpl implements SmppError {
	
	private int errorCode;
	private PDU pdu;
	
	public SmppErrorImpl(int errorCode, PDU pdu){
		this.errorCode = errorCode;
		this.pdu = pdu;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public PDU getMessage() {
		return pdu;
	}

}
