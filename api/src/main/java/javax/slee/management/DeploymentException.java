package javax.slee.management;

/**
 * This exception is thrown by methods defined in {@link DeploymentMBean} when
 * a deployment operation fails due to a problem with the deployable unit.
 */
public class DeploymentException extends Exception {
    /**
     * Create a <code>DeploymentException</code> with a detail message.
     * @param message the detail message.
     */
    public DeploymentException(String message) {
        this(message, null);
    }

    /**
     * Create a <code>DeploymentException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public DeploymentException(String message, Throwable cause) {
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
