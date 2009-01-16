package javax.slee.profile;

/**
 * This exception is thrown by a Profile Management class implemented by the SLEE
 * if an attempt is made to change the value of a profile attribute when the profile
 * is in the read-only state.
 */
public class ReadOnlyProfileException extends RuntimeException {
    /**
     * Create a <code>ReadOnlyProfileException</code> with no detail message.
     */
    public ReadOnlyProfileException() {}

    /**
     * Create a <code>ReadOnlyProfileException</code> with a detail message.
     * @param message the detail message.
     */
    public ReadOnlyProfileException(String message) {
        super(message);
    }
}

