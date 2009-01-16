package javax.slee.resource;

/**
 * This exception is thrown by a {@link SleeEndpoint} if the SLEE is unable to
 * start an activity due to non system-level issues such as an input rate limiting
 * policy.
 * @since SLEE 1.1
 */
public class StartActivityException extends Exception {
    /**
     * Create a <code>StartActivityException</code> with no detail message.
     */
    public StartActivityException() {}

    /**
     * Create a <code>StartActivityException</code> with a detail message.
     * @param message the detail message.
     */
    public StartActivityException(String message) {
        super(message);
    }
}
