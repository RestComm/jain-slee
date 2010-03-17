package javax.slee.management;

/**
 * This exception is thrown by management operations when passed a
 * <code>NotificationSource</code> object that is not recognized by the SLEE.
 * @since SLEE 1.1
 */
public class UnrecognizedNotificationSourceException extends Exception {
    /**
     * Create an <code>UnrecognizedNotificationSourceException</code> with no detail message.
     */
    public UnrecognizedNotificationSourceException() {}

    /**
     * Create an <code>UnrecognizedNotificationSourceException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedNotificationSourceException(String message) {
        super(message);
    }
}
