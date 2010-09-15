package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 *
 */
public interface QueryBroadcastSMResp extends SmppResponse {

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

}
