package org.mobicents.slee.services.sip.common;

/**
 * Exception used to tell caller that a SIP error response should be sent out
 * on the current server transaction. The status code for the response sent 
 * should be the same as getStatusCode()
 */
public class SipSendErrorResponseException extends java.lang.Exception implements java.io.Serializable {
    
    private int statusCode;
    
    public SipSendErrorResponseException(String msg, int statusCode, Throwable t) {
        super(msg,t);
        this.statusCode = statusCode;
    }
    
    public SipSendErrorResponseException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() { return statusCode; }
    
}
