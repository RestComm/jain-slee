package javax.slee.resource;

/**
 * This exception is thrown by a {@link SleeEndpoint} if the SLEE is unable to
 * accept an event for processing due to non system-level issues such as an input
 * rate limiting policy.
 * @since SLEE 1.1
 */
public class FireEventException extends Exception {
    /**
     * Create a <code>FireEventException</code> with no detail message.
     */
    public FireEventException() {}

    /**
     * Create a <code>FireEventException</code> with a detail message.
     * @param message the detail message.
     */
    public FireEventException(String message) {
        super(message);
    }
}
