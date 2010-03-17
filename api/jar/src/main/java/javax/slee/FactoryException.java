package javax.slee;

/**
 * This exception is thrown by activity context and activity context interface factories
 * when the factory cannot perform its function due to a system-level failure.
 */
public class FactoryException extends SLEEException {
    /**
     * Create a <code>FactoryException</code> with a detail message.
     * @param message the detail message.
     */
    public FactoryException(String message) {
        super(message);
    }

    /**
     * Create a <code>FactoryException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }

}

