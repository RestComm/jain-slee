package javax.slee.profile;

/**
 * This exception is thrown by the {@link javax.slee.management.ProfileProvisioningMBean#getProfilesByIndexedAttribute
 * ProfileProvisioningMBean.getProfilesByIndexedAttribute} method when passed the name
 * of an attribute that does not exist in the profile specification.
 */
public class AttributeNotIndexedException extends Exception {
    /**
     * Create an <code>AttributeNotIndexedException</code> with no detail message.
     */
    public AttributeNotIndexedException() {}

    /**
     * Create an <code>AttributeNotIndexedException</code> with a detail message.
     * @param message the detail message.
     */
    public AttributeNotIndexedException(String message) {
        super(message);
    }
}
