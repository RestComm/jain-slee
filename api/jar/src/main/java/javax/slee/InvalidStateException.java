package javax.slee;

/**
 * This exception is thrown by management operations that change the state of
 * some entity (such as the SLEE or a Service) when that entity is not in the
 * correct state to transition to the selected state.
 */
public class InvalidStateException extends Exception {
    /**
     * Create an <code>InvalidStateException</code> with no detail message.
     */
    public InvalidStateException() {}

    /**
     * Create an <code>InvalidStateException</code> with a detail message.
     * @param message the detail message.
     */
    public InvalidStateException(String message) {
        super(message);
    }
}

