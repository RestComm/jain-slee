package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 */
//TODO : Spec says about TLV in section 4.6.2.2 Cancel Broadcast Optional TLVs, but doesnot include TLV field in Table 4-40 cancel_broadcast_sm PDU
public interface CancelBroadcastSM extends SmppRequest {
	public String getServiceType();
	public void setServiceType(String serviceType);
	
	public String getMessageID();
	public void setMessageID(String messageID);	

	public Address getEsmeAddress();
	public void setEsmeAddress(Address address);
}
