/**
 * 
 */
package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.address.SipUri;

import java.text.ParseException;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ViaHeader;

/**
 * @author martins
 *
 */
public class MessageForwardingTransportData {

	private final SipURI sipURI;
	
	private final ViaHeader viaHeader;
	
	private final ContactHeader contactHeader;
		
	/**
	 * @throws ParseException 
	 * @throws InvalidArgumentException 
	 * 
	 */
	public MessageForwardingTransportData(ListeningPoint lp, AddressFactory addressFactory, HeaderFactory headerFactory) throws ParseException, InvalidArgumentException {
		sipURI = new SipUri();
		sipURI.setHost(lp.getIPAddress());
		sipURI.setPort(lp.getPort());
		sipURI.setTransportParam(lp.getTransport());		
		viaHeader = headerFactory.createViaHeader(lp.getIPAddress(),lp.getPort(),lp.getTransport(),null);
		contactHeader = headerFactory.createContactHeader(addressFactory.createAddress(sipURI));	
	}
	
	/**
	 * @return the contactHeader
	 */
	public ContactHeader getContactHeader() {
		return contactHeader;
	}
	
	/**
	 * @return the sipURI
	 */
	public SipURI getSipURI() {
		return sipURI;
	}
	
	/**
	 * @return the viaHeader
	 */
	public ViaHeader getViaHeader() {
		return viaHeader;
	}
	
}
