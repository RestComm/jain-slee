package org.mobicents.slee.services.sip.location.cache;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.ContactHeader;

import org.mobicents.slee.services.sip.common.RegistrationBinding;

public class RegistrationBindingImpl implements Serializable, RegistrationBinding {

    // This date stores the absolute time when this entry will expire
    private Date expiry;

    private float qValue;
    private String callId;
    private long cSeq;
    
    private String comment;
    private String contactAddress;

    private static Logger logger = Logger.getLogger(RegistrationBindingImpl.class.getName());
    
    public RegistrationBindingImpl(String contactAddress, String comment, long expiresDelta, float q, String id, long seq) {
        this.comment = comment;
        this.contactAddress = contactAddress;
        setExpiryDelta(expiresDelta);
        this.qValue = q;
        this.callId = id;
        this.cSeq = seq;
    }

    public long getExpiryAbsolute() { return (expiry.getTime()/1000); }

    /**
     * Returns number of mseconds till this entry expires
     * May be 0 or -ve if already expired
     */
    public int getExpiryDelta() {
        int expiryDelta = (int)(expiry.getTime() - System.currentTimeMillis());
        return (expiryDelta / 1000) + 1;  // +1 to round up to nearest second...
    }
    
    public String getContactAddress() { return contactAddress; }
    public float getQValue() { return qValue; }
    public String getCallId() { return callId; }
    public long getCSeq() { return cSeq; }
    public String getComment() { return comment; }

    public void setExpiryAbsolute(long exp) {
		
        this.expiry = new Date(exp * 1000);
    }

    public void setExpiryDelta(long exp) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, (int)exp);
        expiry = now.getTime();
    }

    public void setContactAddress(String address) {
        this.contactAddress = address;
    }

    public void setQValue(float q) {
        this.qValue = q;
    }

    public void setCallId(String id) {
        this.callId = id;
    }

    public void setCSeq(long seq) {
        this.cSeq = seq;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    private String getScheme(String contactAddress) {
        int i = contactAddress.indexOf(':');
        if (i != -1) {
            return contactAddress.substring(0, i);
        }
        else {
            return null;
        }
    }

    private String getSchemeData(String contactAddress) {
        int i = contactAddress.indexOf(':');
        if (i != -1) {  
            return contactAddress.substring(i + 1, contactAddress.length());
        }
        else {
            return null;
        }
    }  
    
    public ContactHeader getContactHeader(javax.sip.address.AddressFactory af, javax.sip.header.HeaderFactory hf) {
        try {
            long expires = getExpiryDelta();
            if (expires <= 0) {
                // This binding has expired
                return null;
            }
            
            String contactAddress = getContactAddress();

            URI uri = af.createURI(contactAddress);                 // todo : needs scheme:address
            Address nameAddress = af.createAddress(uri);

            javax.sip.header.ContactHeader contact = hf.createContactHeader(nameAddress);
            // String comment = getComment();
            contact.setExpires(getExpiryDelta());
            contact.setQValue(getQValue());
            
            return contact;

        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to getContactHeader()", e);
            return null;
        }

    }


	public String toString() {
		return "RegistratoinBindingImpl[" + contactAddress + "," + comment + "," +
			expiry + "," + qValue + "," + callId + "," + cSeq + "]";
	}
    
}
