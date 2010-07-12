package net.java.slee.resources.smpp;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;

/**
 * 
 * @author amit bhayani
 */
public interface SmppSession {

	public String getSMSCHost();

	public int getSMSPort();

	public SmppTransaction sendRequest(SmppRequest request) throws java.lang.IllegalStateException,
			java.lang.NullPointerException, java.io.IOException;

	public void sendResponse(SmppTransaction txn, SmppResponse response) throws java.lang.IllegalStateException,
			java.lang.NullPointerException, java.io.IOException;
	
	public boolean isAlive();

	/**
	 * @see SmppRequest
	 * @param commandId
	 * @return
	 */
	public SmppRequest createSmppRequest(int commandId);
	
	public Address createAddress(int addTon, int addNpi, String address);

}
