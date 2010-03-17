package javax.slee.resource;

/**
 * This exception can be thrown by the {@link ResourceAdaptor#raVerifyConfiguration}
 * method if the configuration properties supplied by the Administrator are not
 * valid for the resource adaptor.
 * @since SLEE 1.1
 */
public class InvalidConfigurationException extends Exception {
    /**
     * Create an <code>InvalidConfigurationException</code> with a detail message.
     * @param message the detail message.
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }

    /**
     * Create an <code>InvalidConfigurationException</code> with a detail message
     * and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
