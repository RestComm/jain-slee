package javax.slee.usage;

import javax.slee.SLEEException;

/**
 * This exception is thrown if a resource adaptor entity attempts to obtain a usage
 * parameter set from its {@link javax.slee.resource.ResourceAdaptorContext} if the Resource
 * Adaptor did not define a Usage Parameters Interface.
 */
public class NoUsageParametersInterfaceDefinedException extends SLEEException {
    /**
     * Create a <code>NoUsageParametersInterfaceDefinedException</code> with a detail
     * message
     * @param message the detail message.
     */
    public NoUsageParametersInterfaceDefinedException(String message) {
        super(message);
    }
}
