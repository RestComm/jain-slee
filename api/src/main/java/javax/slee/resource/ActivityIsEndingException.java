package javax.slee.resource;

import javax.slee.SLEEException;

/**
 * This exception is thrown by the {@link SleeEndpoint} if an attempt is made to
 * fire an event on an activity that is in the ending state.  
 * @since SLEE 1.1
 */
public class ActivityIsEndingException extends SLEEException {
    /**
     * Create an <code>ActivityIsEndingException</code> with no detail message.
     */
    public ActivityIsEndingException() {
        super(null);
    }

    /**
     * Create an <code>ActivityIsEndingException</code> with a detail message.
     * @param message the detail message.
     */
    public ActivityIsEndingException(String message) {
        super(message);
    }
}
