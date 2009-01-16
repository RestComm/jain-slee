package javax.slee.management;

/**
 * This exception indicates that a resource adaptor entity could not be created
 * because a resource adaptor entity with the same name already exists.
 * @since SLEE 1.1
 */
public class ResourceAdaptorEntityAlreadyExistsException extends Exception {
    /**
     * Create a <code>ResourceAdaptorEntityAlreadyExistsException</code> with
     * no detail message.
     */
    public ResourceAdaptorEntityAlreadyExistsException() {}

    /**
     * Create a <code>ResourceAdaptorEntityAlreadyExistsException</code> with
     * a detail message.
     * @param message the detail message.
     */
    public ResourceAdaptorEntityAlreadyExistsException(String message) {
        super(message);
    }
}
