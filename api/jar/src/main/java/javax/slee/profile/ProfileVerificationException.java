package javax.slee.profile;

/**
 * This exception can be thrown by the {@link Profile#profileVerify} callback method if
 * the state of the profile's CMP fields is not deemed to be valid.
 */
public class ProfileVerificationException extends Exception {
    /**
     * Create a <code>ProfileVerificationException</code> with the a detail message.
     * @param message the detail message.
     */
    public ProfileVerificationException(String message) {
        this(message, null);
    }

    /**
     * Create a <code>ProfileVerificationException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public ProfileVerificationException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * Get the cause (if any) for this exception.
     * @return the cause.
     */
    public Throwable getCause() {
        return cause;
    }


    private final Throwable cause;
}

