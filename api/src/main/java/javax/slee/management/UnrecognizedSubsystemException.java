package javax.slee.management;

/**
 * This exception is thrown by management operations in <code>SleeManagementMBean</code>
 * when passed the name of a SLEE internal component or subsystem that does not exist.
 * @since SLEE 1.1
 */
public class UnrecognizedSubsystemException extends Exception {
    /**
     * Create an <code>UnrecognizedSubsystemException</code> with no detail message.
     */
    public UnrecognizedSubsystemException() {}

    /**
     * Create an <code>UnrecognizedSubsystemException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedSubsystemException(String message) {
        super(message);
    }
}
