package javax.slee.profile;

/**
 * This exception is thrown by the {@link javax.slee.management.ProfileProvisioningMBean#createProfile
 * ProfileProvisioningMBean.createProfile} method when passed the name of a
 * profile that is already in use.
 */
public class ProfileAlreadyExistsException extends Exception {
    /**
     * Create an <code>ProfileAlreadyExistsException</code> with no detail message.
     */
    public ProfileAlreadyExistsException() {}

    /**
     * Create an <code>ProfileAlreadyExistsException</code> with a detail message.
     * @param message the detail message.
     */
    public ProfileAlreadyExistsException(String message) {
        super(message);
    }
}
