package org.mobicents.slee.resource.parlay.util.crypto;


public class RSAUtilException extends Exception {

    public RSAUtilException(Throwable cause) {
        super();
        this.cause = cause;
    }

    public RSAUtilException( String message ) {
        super(message);
        this.cause = null;
    }

    public RSAUtilException( String message, Throwable cause ) {
        super(message);
        this.cause = cause;
    }

    private static final String lineSeparator =
        System.getProperty("line.separator");
    private transient Throwable cause = null;

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
