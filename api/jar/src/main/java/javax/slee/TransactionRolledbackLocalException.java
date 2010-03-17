package javax.slee;

/**
 * This exception is thrown by the SLEE when a method invoked on an SbbLocalObject
 * throws an unchecked exception, or when a method invoked on a required-transactional
 * facility is invoked without a transaction context and the newly created transaction
 * fails to commit.
 */
public class TransactionRolledbackLocalException extends SLEEException {
    /**
     * Create a <code>TransactionRolledbackLocalException</code> with a detail message.
     * @param message the detail message.
     */
    public TransactionRolledbackLocalException(String message) {
        super(message);
    }

    /**
     * Create a <code>TransactionRolledbackLocalException</code> with a detail message and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public TransactionRolledbackLocalException(String message, Throwable cause) {
        super(message, cause);
    }

}

