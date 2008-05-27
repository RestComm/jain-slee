package org.mobicents.slee.services.sip.common;

import javax.sip.header.ContactHeader;

public interface RegistrationBinding {

	
	public long getExpiryAbsolute();
    public int getExpiryDelta();
    public String getContactAddress() ;
    public float getQValue() ;
    public String getCallId();
    public long getCSeq();
    public String getComment();
    public ContactHeader getContactHeader(javax.sip.address.AddressFactory af, javax.sip.header.HeaderFactory hf);
	
}
