package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 */
public interface CancelSM extends SmppRequest {

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract String getMessageID();

	public abstract void setMessageID(String messageID);

	public abstract Address getDestAddress();

	public abstract void setDestAddress(Address address);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);
}
