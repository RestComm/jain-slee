package javax.slee.profile;

/**
 * This exception is thrown by the management operations in
 * <code>ProfileProvisioningMBean</code> and profile CMP accessor methods when passed the
 * name of a profile that does not exist within the specified profile table.
 */
public class UnrecognizedProfileNameException extends Exception {
    /**
     * Create an <code>UnrecognizedProfileNameException</code> with no detail message.
     */
    public UnrecognizedProfileNameException() {}

    /**
     * Create an <code>UnrecognizedProfileNameException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedProfileNameException(String message) {
        super(message);
    }
}
