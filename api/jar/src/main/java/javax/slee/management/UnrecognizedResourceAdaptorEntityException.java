package javax.slee.management;

/**
 * This exception is thrown by management operations in <code>ResourceManagementMBean</code>
 * when passed the name of a resource adaptor entity that does not exist in the SLEE.
 * @since SLEE 1.1
 */
public class UnrecognizedResourceAdaptorEntityException extends Exception {
    /**
     * Create an <code>UnrecognizedResourceAdaptorEntityException</code> with no detail message.
     */
    public UnrecognizedResourceAdaptorEntityException() {}

    /**
     * Create an <code>UnrecognizedResourceAdaptorEntityException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedResourceAdaptorEntityException(String message) {
        super(message);
    }
}
