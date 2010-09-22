package net.java.slee.resources.smpp;

import java.util.Calendar;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.util.AbsoluteSMPPDate;
import net.java.slee.resources.smpp.util.RelativeSMPPDate;

/**
 * In order to exchange short messages, a SMPP session must be established between the ESME and Message Centre or SMPP
 * Routing Entity where appropriate.
 * 
 * @author amit bhayani
 */
public interface SmppSession {

	/**
	 * Get the unique session ID.
	 * 
	 * @return
	 */
	public String getSessionId();

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

	public AbsoluteSMPPDate createAbsoluteSMPPDate(Calendar calendar, boolean hasTz);

	public RelativeSMPPDate createRelativeSMPPDate(int years, int months, int days, int hours, int minutes, int seconds);

}
