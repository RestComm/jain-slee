package javax.slee.resource;

import javax.slee.SLEEException;

/**
 * This exception is thrown by the {@link SleeEndpoint} if an attempt is made to
 * interact with an activity that is not known by the SLEE.  
 * @since SLEE 1.1
 */
public class UnrecognizedActivityHandleException extends SLEEException {
    /**
     * Create an <code>UnrecognizedActivityHandleException</code> with a detail message.
     * @param message the detail message
     */
    public UnrecognizedActivityHandleException(String message) {
        super(message);
    }

    /**
     * Create an <code>UnrecognizedActivityHandleException</code> with a detail message
     * and cause.
     * @param message the detail message
     * @param cause the reason this exception was thrown.
     */
    public UnrecognizedActivityHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
