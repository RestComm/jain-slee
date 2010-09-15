package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 *
 */
public interface QueryBroadcastSM extends SmppRequest {

	public abstract String getMessageID();
	public abstract void setMessageID(String messageID);

	public abstract Address getDestAddress();
	public abstract void setDestAddress(Address address);

}
