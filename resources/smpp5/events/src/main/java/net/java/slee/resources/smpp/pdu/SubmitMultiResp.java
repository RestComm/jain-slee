package net.java.slee.resources.smpp.pdu;


/**
 * 
 * @author amit bhayani
 * 
 */
public interface SubmitMultiResp extends SmppResponse {

	public String getMessageID();

	public void setMessageID(String messageID);

	public int getNumUnsuccess();

	public java.util.List<ErrorAddress> getUnsuccessSME();
	
	public void addErrorAddress(ErrorAddress errAddress);
	
	

}
