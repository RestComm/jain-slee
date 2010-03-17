package javax.slee;

/**
 * This exception is thrown by the {@link ChildRelation#create} method if an attempt
 * to create an SBB entity cannot be completed successfully.  The {@link Sbb#sbbCreate}
 * and {@link Sbb#sbbPostCreate} methods include this exception in their <code>throws</code>
 * clause to give the SBB entity being created a chance to abort the create operation if
 * conditions are not met.
 */
public class CreateException extends Exception {
    /**
     * Create a <code>CreateException</code> with a detail message.
     * @param message the detail message.
     */
    public CreateException(String message) {
        this(message, null);
    }

    /**
     * Create a <code>CreateException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public CreateException(String message, Throwable cause) {
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

