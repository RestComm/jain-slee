package javax.slee.management;

import javax.slee.InvalidStateException;

/**
 * This exception is thrown by the <code>ServiceManagementMBean</code> if an attempt
 * is made to activate a service for which a required resource adaptor entity link name
 * binding either doesn't exist or is bound to a resource adaptor entity that does not
 * implement the resource adaptor type expected by the service.
 * @since SLEE 1.1
 */
public class InvalidLinkNameBindingStateException extends InvalidStateException {
    /**
     * Create an <code>InvalidLinkNameBindingStateException</code> with no detail message.
     */
    public InvalidLinkNameBindingStateException() {}

    /**
     * Create an <code>InvalidLinkNameBindingStateException</code> with a detail message.
     * @param message the detail message.
     */
    public InvalidLinkNameBindingStateException(String message) {
        super(message);
    }
}
