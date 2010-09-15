package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 * 
 */
public interface DeliverSMResp extends SmppResponse {

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

}
