package javax.slee;

/**
 * This exception is thrown by methods in <code>SbbContext</code> if an SBB entity
 * attempts to obtain or modify the event mask for an activity it is not attached to.
 * <p>
 * As of SLEE 1.1 this class extends {@link javax.slee.SLEEException} instead of {@link Exception}.
 */
public class NotAttachedException extends SLEEException {
    /**
     * Create a <code>NotAttachedException</code> with no detail message.
     */
    public NotAttachedException() {
        super(null);
    }

    /**
     * Create a <code>NotAttachedException</code> with a detail message.
     * @param message the detail message.
     */
    public NotAttachedException(String message) {
        super(message);
    }
}

