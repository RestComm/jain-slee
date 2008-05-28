package org.mobicents.slee.resource.sip;

import javax.sip.SipProvider;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;


public interface SipResourceAdaptorSbbInterface {
	public abstract AddressFactory getAddressFactory();

    public abstract HeaderFactory getHeaderFactory();

    public abstract MessageFactory getMessageFactory();

    public abstract SipProvider getSipProvider();
    
    public String getHostAddress();
    
    public int getHostPort();
    
    public String[] getTransports();
}
