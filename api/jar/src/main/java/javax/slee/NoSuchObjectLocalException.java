package javax.slee;

/**
 * This exception is thrown if an attempt is made to invoke a method on an SBB entity
 * that no longer exists.
 */
public class NoSuchObjectLocalException extends SLEEException {
    /**
     * Create a <code>NoSuchObjectLocalException</code> with a detail message.
     * @param message the detail message.
     */
    public NoSuchObjectLocalException(String message) {
        super(message);
    }

    /**
     * Create a <code>NoSuchObjectLocalException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public NoSuchObjectLocalException(String message, Throwable cause) {
        super(message, cause);
    }
}

