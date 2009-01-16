package javax.slee.facilities;

import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.resource.FireableEventType;

/**
 * The Event Lookup Facility is used by resource adaptors to obtain information about the event
 * types that it may fire to the SLEE.  A resource adaptor may obtain an instance of the Event
 * Lookup Facility via the {@link javax.slee.resource.ResourceAdaptorContext#getEventLookupFacility()}
 * method.
 * @since SLEE 1.1
 */
public interface EventLookupFacility {
    /**
     * Get a <code>FireableEventType</code> object for a given event type.  The
     * <code>FireableEventType</code> object is used to identify the type of an event
     * fired to the SLEE by a resource adaptor.
     * <p>
     * The SLEE need only provide a resource adaptor with information about the event
     * types that the resource adaptor may fire.  Generally this is limited to the
     * resource adaptor types implemented by the resource adaptor.  However a resource
     * adaptor may be able to fire events of any type if its deployment descriptor
     * has disabled this limitation, and in such cases the Event Lookup Facility should
     * provide the resource adaptor with information about all the event types that are
     * installed in the SLEE.
     * <p>
     * This method is a non-transactional method.
     * @param eventType the event type component identifier for the event type.
     * @return a <code>FireableEventType</code> object for the event type.
     * @throws NullPointerException if <code>eventType</code> is <code>null</code>.
     * @throws UnrecognizedEventException if <code>eventType</code> does not identify an
     *        event type installed in the SLEE.  This exception may also be thrown if the
     *        event type is not referenced by any of the resource adaptor types implemented
     *        by the resource adaptor (and is hence theorectically unrecognizable to that
     *        resource adaptor) unless this check has been disabled in the resource
     *        adaptor's deployment descriptor.
     * @throws FacilityException if the <code>FireableEventType</code> object could not be
     *        obtained due to a system-level failure.
     */
    public FireableEventType getFireableEventType(EventTypeID eventType)
        throws NullPointerException, UnrecognizedEventException, FacilityException;
}
