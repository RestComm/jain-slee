package javax.slee.management;

/**
 * This exception is thrown when a resource adaptor entity unbinding operation fails because
 * no resource adaptor entity is bound to the specified link name.
 * @since SLEE 1.1
 */
public class UnrecognizedLinkNameException extends Exception {
    /**
     * Constructs a <code>UnrecognizedLinkNameException</code> with no detail message.
     */
    public UnrecognizedLinkNameException() {}

    /**
     * Constructs a <code>UnrecognizedLinkNameException</code> with a detail message.
     * @param message the message.
     */
    public UnrecognizedLinkNameException(String message) {
        super(message);
    }
}
