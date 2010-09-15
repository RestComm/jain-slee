package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 * 
 */
public interface AlertNotification extends SmppRequest {

	public Address getEsmeAddress();

	public Address getSourceAddress();

	public void setEsmeAddress(Address address);

	public void setSourceAddress(Address address);

}
