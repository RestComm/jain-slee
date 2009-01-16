package javax.slee.usage;

/**
 * This exception is thrown by a <code>UsageMBean</code> when an attempt
 * is made to refer to an usage parameter set that does not exist.
 */
public class UnrecognizedUsageParameterSetNameException extends Exception {
    /**
     * Create an <code>UnrecognizedUsageParameterSetNameException</code> with no detail message.
     */
    public UnrecognizedUsageParameterSetNameException() {}

    /**
     * Create an <code>UnrecognizedUsageParameterSetNameException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedUsageParameterSetNameException(String message) {
        super(message);
    }
}

