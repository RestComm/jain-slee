package net.java.slee.resource.sip;

import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.address.AddressFactory;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;

public interface SleeSipProvider extends SipProvider {
	public AddressFactory getAddressFactory();

	public HeaderFactory getHeaderFactory();

	public MessageFactory getMessageFactory();

	public DialogActivity getNewDialog(Address from, Address to)
			throws SipException;

	public DialogActivity getNewDialog(DialogActivity incomingDialog,
			boolean useSameCallId) throws SipException;

	public boolean isLocalSipURI(SipURI uri);

	public boolean isLocalHostname(String host);

	public SipURI getLocalSipURI(String transport);

	public ViaHeader getLocalVia(String transport, String branch);
}