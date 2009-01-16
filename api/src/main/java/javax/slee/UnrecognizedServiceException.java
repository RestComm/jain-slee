package javax.slee;

/**
 * This exception is thrown by management operations when passed a <code>ServiceID</code>
 * object that is not recognized by the SLEE, or does not identify a Service installed
 * in the SLEE.
 */
public class UnrecognizedServiceException extends UnrecognizedComponentException {
    /**
     * Create an <code>UnrecognizedServiceException</code> with no detail message.
     */
    public UnrecognizedServiceException() {}

    /**
     * Create an <code>UnrecognizedServiceException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedServiceException(String message) {
        super(message);
    }
}
