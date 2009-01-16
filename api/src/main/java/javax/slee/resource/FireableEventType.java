package javax.slee.resource;

import javax.slee.EventTypeID;

/**
 * The <code>FireableEventType</code> interface identifies the type of an event
 * fired to the SLEE.
 * @since SLEE 1.1
 */
public interface FireableEventType {
    /**
     * Get the component identifier of the event type represented by this
     * <code>FireableEventType</code>.
     * <p>
     * This method is a non-transactional method.
     * @return the event type component identifier.
     */
    public EventTypeID getEventType();

    /**
     * Get the fully-qualified name of the event class for the event type represented
     * by this <code>FireableEventType</code>.  This is the class name specified in the
     * event type's deployment descriptor.
     * <p>
     * This method is a non-transactional method.
     * @return the fully-qualified class name of the event type.
     */
    public String getEventClassName();

    /**
     * Get a classloader that can be used to load the event class for the event type
     * represented by this <code>FireableEventType</code>.
     * <p>
     * This method is a non-transactional method.
     * @return a classloader for the event class. 
     */
    public ClassLoader getEventClassLoader();
}
