package javax.slee.facilities;

/**
 * This exception is thrown by the Activity Context Naming Facility when an attempt
 * is made to bind an Activity Context to a name that is already bound to some
 * Activity Context.
 */
public class NameAlreadyBoundException extends Exception {
    /**
     * Create an <code>NameAlreadyBoundException</code> with a detail message.
     * @param message the detail message
     */
    public NameAlreadyBoundException(String message) {
        super(message);
    }
}

