package javax.slee.resource;

import javax.slee.SLEEException;

/**
 * This exception is thrown by a {@link SleeEndpoint} if an attempt is made to
 * start an activity that is already known by the SLEE.
 * @since SLEE 1.1
 */
public class ActivityAlreadyExistsException extends SLEEException {
    /**
     * Create an <code>ActivityAlreadyExistsException</code> with a detail message.
     * @param message the detail message.
     */
    public ActivityAlreadyExistsException(String message) {
        super(message);
    }
}
