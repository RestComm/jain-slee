package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 *
 */
public interface SmppError {

	public static final int SMPP_TIMEOUT_RESPONSE_RECEIVED = 1;
	public static final int SMPP_TIMEOUT_RESPONSE_SENT = 2;

	public int getErrorCode();

	public PDU getMessage();
}
