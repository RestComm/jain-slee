package javax.slee.profile;

/**
 * This exception is thrown by the <code>ProfileFacility</code> and
 * <code>ProfileProvisioningMBean</code> when passed the name of a profile
 * table that does not exist.
 */
public class UnrecognizedProfileTableNameException extends Exception {
    /**
     * Create an <code>UnrecognizedProfileTableNameException</code> with no detail message.
     */
    public UnrecognizedProfileTableNameException() {}

    /**
     * Create an <code>UnrecognizedProfileTableNameException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedProfileTableNameException(String message) {
        super(message);
    }
}
