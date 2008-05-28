package org.mobicents.slee.resource.parlay.fw.access;


/**
 * Signals that an exception occured within the TSMBean. This exception may wrap
 * a lower level API exceptions.
 */
public class TSMBeanException extends Exception {

    private static final String lineSeparator =
        System.getProperty("line.separator");
    
    Throwable cause = null;

    /**
     * @see java.lang.Throwable#Throwable(String)
     */
    public TSMBeanException( String message ) {
        super(message);
    }

    /**
     * Method TSMBeanException.
     * @param cause
     */
    public TSMBeanException(Exception cause) {
        super();
        this.cause = cause;
    }

    /**
     * Method TSMBeanException.
     * @param message
     * @param cause
     */
    public TSMBeanException( String message, Exception cause ) {
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
