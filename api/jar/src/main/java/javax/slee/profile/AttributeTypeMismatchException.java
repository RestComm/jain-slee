package javax.slee.profile;

/**
 * This exception is thrown by methods in {@link javax.slee.profile.ProfileFacility}
 * and {@link javax.slee.management.ProfileProvisioningMBean} when attempting to find
 * profiles by an indexed attribute, but the attribute value supplied to the method is
 * not of the correct Java type for the attribute.
 */
public class AttributeTypeMismatchException extends Exception {
    /**
     * Create a <code>AttributeTypeMismatchException</code> with a detail message.
     * @param message the detail message.
     */
    public AttributeTypeMismatchException(String message) {
        super(message);
    }
}
