package javax.slee;

/**
 * This exception class is the base class for the exceptions that report that
 * a <code>ComponentID</code> (or a deritive interface) object does not identify
 * a component installed in the SLEE.
 */
public class UnrecognizedComponentException extends Exception {
    /**
     * Create an <code>UnrecognizedComponentException</code> with no detail message.
     */
    public UnrecognizedComponentException() {}

    /**
     * Create an <code>UnrecognizedComponentException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedComponentException(String message) {
        super(message);
    }
}
