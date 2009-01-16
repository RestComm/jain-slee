package javax.slee.management;

/**
 * This exception is thrown by the {@link SleeProviderFactory} to indicate that
 * a <code>SleeProvider</code> peer class could not be instantiated using the
 * class name provided.
 * <p>
 * Possible reasons that the peer class could not be instantiated include:
 * <ul>
 *   <li>the given class name could not be found in the class path
 *   <li>the class did not have a public no-arg constructor
 *   <li>the class did not implement the {@link SleeProvider} interface
 * </ul>
 */
public class PeerUnavailableException extends Exception {
    /**
     * Create a <code>PeerUnavailableException</code> with a detail message.
     * @param message the detail message.
     */
    public PeerUnavailableException(String message) {
        this(message, null);
    }

    /**
     * Create a <code>PeerUnavailableException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public PeerUnavailableException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * Get the cause for this exception.
     * @return the cause.
     */
    public Throwable getCause() {
        return cause;
    }


    private final Throwable cause;
}
