package javax.slee.profile;

/**
 * This exception is thrown by the {@link javax.slee.management.ProfileProvisioningMBean#createProfileTable
 * ProfileProvisioningMBean.createProfileTable} method when passed the name of a
 * profile table that is already in use.
 */
public class ProfileTableAlreadyExistsException extends Exception {
    /**
     * Create an <code>ProfileTableAlreadyExistsException</code> with no detail message.
     */
    public ProfileTableAlreadyExistsException() {}

    /**
     * Create an <code>ProfileTableAlreadyExistsException</code> with a detail message.
     * @param message the detail message.
     */
    public ProfileTableAlreadyExistsException(String message) {
        super(message);
    }
}
