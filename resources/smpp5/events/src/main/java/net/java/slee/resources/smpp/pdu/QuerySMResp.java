package net.java.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.util.SMPPDate;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface QuerySMResp extends SmppResponse {

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public abstract SMPPDate getFinalDate();

	public abstract void setFinalDate(SMPPDate date);

	/**
	 * @see MessageState
	 * @return
	 */
	public abstract int getMessageState();

	public abstract void setMessageState(int messageState);

	public abstract int getErrorCode();

	public abstract void setErrorCode(int errorCode);

}
