package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 *
 */
public abstract class SmppError {

	public static final int SMPP_TIMEOUT_RESPONSE_RECEIVED = 1;
	public static final int SMPP_TIMEOUT_RESPONSE_SENT = 2;

	public abstract int getErrorCode();

	public abstract PDU getMessage();
}
