package javax.slee.resource;

import javax.slee.SLEEException;

/**
 * This exception is thrown by the fire-event methods in {@link SleeEndpoint} if the
 * Resource Adaptor attempts to fire an event with an illegal event identifier.  An
 * illegal event identifier is one which represents an event type that the Resource
 * Adaptor is not permitted to fire.  A Resource Adaptor may only fire events of event
 * types referenced by the resource adaptor types it implements unless this restriction
 * has been disabled in the resource adaptor's deployment descriptor.
 * @since SLEE 1.1
 */
public class IllegalEventException extends SLEEException {
    /**
     * Create an <code>IllegalEventException</code> with a detail message.
     * @param message the detail message.
     */
    public IllegalEventException(String message) {
        super(message);
    }

    /**
     * Create an <code>IllegalEventException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public IllegalEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
