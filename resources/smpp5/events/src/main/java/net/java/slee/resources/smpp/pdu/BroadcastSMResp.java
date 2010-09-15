package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 *
 */
public interface BroadcastSMResp extends SmppResponse {

	public String getMessageID();

	public void setMessageID(String messageID);

}
