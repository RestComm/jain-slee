package javax.slee.facilities;

import javax.slee.SLEEException;

/**
 * This exception is thrown by facilities when a system-level failure is encountered
 * while handling an SBB request.
 */
public class FacilityException extends SLEEException {
    /**
     * Create a <code>FacilityException</code> with a detail message.
     * @param message the detail message.
     */
    public FacilityException(String message) {
        super(message);
    }

    /**
     * Create a <code>FacilityException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public FacilityException(String message, Throwable cause) {
        super(message, cause);
    }

}

