package org.mobicents.slee.resource.parlay.fw.application;


/**
 * Signals that an exception occured within the SAMBean. This exception may wrap
 * a lower level PCP API exception.
 */
public class SABeanException extends Exception {

    private static final String lineSeparator =
        System.getProperty("line.separator");
    Throwable cause = null;

    /**
     * @see java.lang.Throwable#Throwable(String)
     */
    public SABeanException( String message ) {
        super(message);
    }

    /**
     * Method SABeanException.
     * @param cause
     */
    public SABeanException(Exception cause) {
        super();
        this.cause = cause;
    }

    /**
     * Method SABeanException.
     * @param message
     * @param cause
     */
    public SABeanException( String message, Exception cause ) {
        super(message);
        this.cause = cause;
    }
    
    /**
     * Returns the cause.
     * @return Throwable
     */
    public Throwable getCause() {
        return (cause==this ? null : cause);
    }
    
    
    /**
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {
        String message = super.getLocalizedMessage();
        message = (cause != null) ? message + lineSeparator + "Root Cause : " + cause.getLocalizedMessage() : message;
        return message;
    }
}
