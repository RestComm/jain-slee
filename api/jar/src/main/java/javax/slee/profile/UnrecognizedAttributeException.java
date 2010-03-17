package javax.slee.profile;

/**
 * This exception is thrown by the {@link javax.slee.management.ProfileProvisioningMBean#getProfilesByIndexedAttribute
 * ProfileProvisioningMBean.getProfilesByIndexedAttribute} method when passed the name
 * of an attribute that does not exist in the profile specification.
 */
public class UnrecognizedAttributeException extends Exception {
    /**
     * Create an <code>UnrecognizedAttributeException</code> with no detail message.
     */
    public UnrecognizedAttributeException() {}

    /**
     * Create an <code>UnrecognizedAttributeException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedAttributeException(String message) {
        super(message);
    }
}
