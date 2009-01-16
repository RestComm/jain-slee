package javax.slee.management;

/**
 * This exception is thrown by methods defined in the SLEE's management API
 * if a management operation could not be successfully completed due to an unexpected
 * system-level failure.
 */
public class ManagementException extends Exception {
    /**
     * Create a <code>ManagementException</code> with a detail message.
     * @param message the detail message.
     */
    public ManagementException(String message) {
        this(message, null);
    }

    /**
     * Create a <code>ManagementException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public ManagementException(String message, Throwable cause) {
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
