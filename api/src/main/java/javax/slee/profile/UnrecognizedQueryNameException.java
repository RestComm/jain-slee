package javax.slee.profile;

/**
 * This exception is thrown by the <code>ProfileFacility</code> and
 * <code>ProfileProvisioningMBean</code> when passed the name of a static query
 * that has not been defined.
 * @since SLEE 1.1
 */
public class UnrecognizedQueryNameException extends Exception {
    /**
     * Create an <code>UnrecognizedQueryNameException</code> with no detail message.
     */
    public UnrecognizedQueryNameException() {}

    /**
     * Create an <code>UnrecognizedQueryNameException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedQueryNameException(String message) {
        super(message);
    }
}
