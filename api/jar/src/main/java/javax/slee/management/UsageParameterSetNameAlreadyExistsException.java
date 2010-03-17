package javax.slee.management;

/**
 * This exception is thrown by a <code>ServiceUsageMBean</code> when an
 * attempt is made to create an SBB parameter set with a name that has
 * already been used.
 */
public class UsageParameterSetNameAlreadyExistsException extends Exception {
    /**
     * Create a <code>UsageParameterSetNameAlreadyExistsException</code> with no detail message.
     */
    public UsageParameterSetNameAlreadyExistsException() {}

    /**
     * Create a <code>UsageParameterSetNameAlreadyExistsException</code> with a detail message.
     * @param message the detail message.
     */
    public UsageParameterSetNameAlreadyExistsException(String message) {
        super(message);
    }
}

