package org.mobicents.slee.resource.parlay.fw;

/**
 * Signals that an exception occured within the FwSession.
 */
public class FwSessionException extends Exception {

    private static final String lineSeparator = System
            .getProperty("line.separator");

    Throwable cause = null;

    /**
     * Method FwSessionException.
     * 
     * @param cause
     */
    public FwSessionException(Exception cause) {
        super();
        this.cause = cause;
    }

    /**
     * @see java.lang.Throwable#Throwable(String)
     */
    public FwSessionException(String message) {
        super(message);
    }

    /**
     * Method FwSessionException.
     * 
     * @param message
     * @param cause
     */
    public FwSessionException(String message, Exception cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * Returns the cause.
     * 
     * @return Throwable
     */
    public Throwable getCause() {
        return (cause == this ? null : cause);
    }

    /**
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {
        String message = super.getLocalizedMessage();
        message = (cause != null) ? message + lineSeparator + "Root Cause : "
                + cause.getLocalizedMessage() : message;
        return message;
    }

}