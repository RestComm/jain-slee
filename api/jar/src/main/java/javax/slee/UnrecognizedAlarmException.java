package javax.slee;

/**
 * This exception is thrown when an attempt is made to obtain or reference an alarm
 * using an alarm ID that is not recognized by the SLEE, or does not represent an alarm
 * known by the SLEE.
 * @since SLEE 1.1
 */
public class UnrecognizedAlarmException extends Exception {
    /**
     * Create an <code>UnrecognizedAlarmException</code> with no detail message.
     */
    public UnrecognizedAlarmException() {}

    /**
     * Create an <code>UnrecognizedAlarmException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedAlarmException(String message) {
        super(message);
    }
}
