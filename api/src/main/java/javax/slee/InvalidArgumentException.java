package javax.slee;

/**
 * This exception class is thrown by management operations when given an illegal
 * argument such as a zero-length string or invalid numerical value.
 */
public class InvalidArgumentException extends Exception {
    /**
     * Create an <code>InvalidArgumentException</code> with no detail message.
     */
    public InvalidArgumentException() {}

    /**
     * Create an <code>InvalidArgumentException</code> with a detail message.
     * @param message the detail message.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}
