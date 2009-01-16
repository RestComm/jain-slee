package javax.slee.facilities;

/**
 * This exception is thrown by the Activity Context Naming Facility when an attempt
 * is made to unbind an Activity Context from a name that is not bound.
 */
public class NameNotBoundException extends Exception {
    /**
     * Create an <code>NameNotBoundException</code> with a detail message.
     * @param message the detail message
     */
    public NameNotBoundException(String message) {
        super(message);
    }
}

