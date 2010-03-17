package javax.slee.management;

import javax.slee.UnrecognizedComponentException;

/**
 * This exception is thrown by management operations when passed a
 * <code>ResourceAdaptorID</code> object that is not recognized by the SLEE,
 * or does not identify a resource adaptor installed in the SLEE.
 * @since SLEE 1.1
 */
public class UnrecognizedResourceAdaptorException extends UnrecognizedComponentException {
    /**
     * Create an <code>UnrecognizedResourceAdaptorException</code> with no detail message.
     */
    public UnrecognizedResourceAdaptorException() {}

    /**
     * Create an <code>UnrecognizedResourceAdaptorException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedResourceAdaptorException(String message) {
        super(message);
    }
}
