package javax.slee.profile;

import javax.slee.UnrecognizedComponentException;

/**
 * This exception is thrown by management operations when passed a
 * <code>ProfileSpecificationID</code> object that is not recognized by the SLEE,
 * or does not identify a profile specification installed in the SLEE.
 */
public class UnrecognizedProfileSpecificationException extends UnrecognizedComponentException {
    /**
     * Create an <code>UnrecognizedProfileSpecificationException</code> with no detail message.
     */
    public UnrecognizedProfileSpecificationException() {}

    /**
     * Create an <code>UnrecognizedProfileSpecificationException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedProfileSpecificationException(String message) {
        super(message);
    }
}
