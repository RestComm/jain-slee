package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 * 
 */
public interface QuerySM extends SmppRequest {

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

}
