package javax.slee;

/**
 * This exception is thrown by the SLEE when an mandatory transactional method
 * is invoked without a valid transaction context.
 */
public class TransactionRequiredLocalException extends SLEEException {
    /**
     * Create a <code>TransactionRequiredLocalException</code> with a detail message.
     * @param message the detail message.
     */
    public TransactionRequiredLocalException(String message) {
        super(message);
    }

    /**
     * Create a <code>TransactionRequiredLocalException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public TransactionRequiredLocalException(String message, Throwable cause) {
        super(message, cause);
    }

}

