package javax.slee;

/**
 * This exception is thrown if the event names array passed to the {@link
 * SbbContext#maskEvent SbbContext.maskEvent} method contains the name of an
 * event that is not received by the SBB.
 */
public class UnrecognizedEventException extends UnrecognizedComponentException {
    /**
     * Create an <code>UnrecognizedEventException</code> with no detail message.
     */
    public UnrecognizedEventException() {}

    /**
     * Create an <code>UnrecognizedEventException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedEventException(String message) {
        super(message);
    }
}

