package javax.slee.management;

/**
 * This exception is thrown by management operations when passed a URL that does
 * not correspond to an installed deployable unit, or when passed a
 * <code>DeployableUnitID</code> object that is not recognized by the SLEE, or does
 * not identify a deployable unit installed in the SLEE.
 * <p>
 * As of SLEE 1.1 this class extends <code>java.lang.Exception</code> instead of
 * {@link javax.slee.UnrecognizedComponentException}.
 */
public class UnrecognizedDeployableUnitException extends Exception {
    /**
     * Create an <code>UnrecognizedDeployableUnitException</code> with no detail message.
     */
    public UnrecognizedDeployableUnitException() {}

    /**
     * Create an <code>UnrecognizedDeployableUnitException</code> with a detail message.
     * @param message the detail message.
     */
    public UnrecognizedDeployableUnitException(String message) {
        super(message);
    }
}
