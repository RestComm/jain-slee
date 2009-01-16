package javax.slee;

/**
 * This exception is thrown by management operations when passed a <code>SbbID</code>
 * object that is not recognized by the SLEE, or does not identify an SBB installed
 * in the SLEE.
 */
public class UnrecognizedSbbException extends UnrecognizedComponentException {
    /**
     * Create an <code>UnrecognizedSbbException</code> with no detail message.
     */
    public UnrecognizedSbbException() {}

    /**
     * Create an <code>UnrecognizedSbbException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedSbbException(String message) {
        super(message);
    }
}
