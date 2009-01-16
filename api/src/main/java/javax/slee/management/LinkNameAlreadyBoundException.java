package javax.slee.management;

/**
 * This exception is thrown when a resource adaptor entity binding operation fails because
 * a resource adaptor entity is already bound to the specified link name.
 * @since SLEE 1.1
 */
public class LinkNameAlreadyBoundException extends Exception {
    /**
     * Constructs an <code>LinkNameAlreadyBoundException</code> with no detail message.
     */
    public LinkNameAlreadyBoundException() {}

    /**
     * Constructs an <code>LinkNameAlreadyBoundException<code> with a detail message.
     * @param message the detail message.
     */
    public LinkNameAlreadyBoundException(String message) {
        super(message);
    }
}
