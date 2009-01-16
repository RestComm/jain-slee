package javax.slee.connection;

import javax.resource.ResourceException;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;

/**
 * An application handle to a physical event-delivery connection to a SLEE.
 * This interface is used by EJB components external to a SLEE to communicate
 * with the SLEE's event routing subsystem. A SLEE vendor provides an
 * implementation of this interface if they support external interoperability.
 *<p>
 * Instances of SleeConnection are created by an EJB application calling
 * {@link SleeConnectionFactory#getConnection}
 *<p>
 * Implementations may defer validation of arguments and detection of
 * communication errors beyond the return of a method invocation.
 * Clients of this interface should not depend on exceptions being thrown in
 * the face of these errors.
 *
 * @see SleeConnectionFactory
 * @see ExternalActivityHandle
 */
public interface SleeConnection {
    /**
     * Creates a new external activity handle. The returned handle may be used
     * to fire events to an external SLEE activity. Events fired using a
     * particular external activity handle will be delivered to a single
     * activity within the SLEE, with the exception that the SLEE may end the
     * activity if no SBBs remain attached, and recreate a new activity for a
     * subsequent event fired with the same handle.
     *
     * @return a new, unique, ExternalActivityHandle object
     * @throws ResourceException if this connection is closed, or an activity
     *  handle could not be allocated due to a system-level failure.
     */
    public ExternalActivityHandle createActivityHandle()
        throws ResourceException;

    /**
     * Retrieves an EventTypeID that identifies a particular SLEE event.
     * This method does not necessarily validate the existence of the event
     * type in the SLEE.
     *
     * @param name the event's name
     * @param vendor the event's vendor
     * @param version the event's version
     * @return an EventTypeID object representing the event
     * @throws UnrecognizedEventException if the SleeConnection determines
     *  there is no corresponding event known to the SLEE.
     * @throws ResourceException if this connection is closed, or the
     *  relevant EventTypeID could not be located due to a system-level
     *  failure.
     */
    EventTypeID getEventTypeID(String name, String vendor, String version)
        throws UnrecognizedEventException, ResourceException;

    /**
     * Fires an event on an external activity. If a transaction is in
     * progress,  the event is only delivered to the SLEE activity if the
     * enclosing transaction commits.
     * 
     * @param event the event to fire; must be serializable.
     * @param eventType an EventTypeID from {@link #getEventTypeID}
     *  indicating the SLEE event type of the event being fired.
     * @param handle an ExternalActivityHandle returned by
     *  {@link #createActivityHandle} indicating the SLEE activity to fire
     *  the event on.
     * @param address an optional address to use in event routing; may be
     *  <code>null</code>.
     *
     * @throws NullPointerException if event, eventType, or activityHandle are
     *   <code>null</code>.
     * @throws UnrecognizedEventException if <code>eventType</code> is not a
     *   valid SLEE event type.
     * @throws UnrecognizedActivityException if <code>activityHandle</code> is
     *   not a valid external activity handle
     * @throws ResourceException if this connection is closed, or the event
     *   could not be fired due to a system-level failure.
     */
    public void fireEvent(Object event, EventTypeID eventType, ExternalActivityHandle handle, Address address)
        throws NullPointerException, UnrecognizedActivityException,
               UnrecognizedEventException, ResourceException;

    /**
     * Closes this SleeConnection. Any further use of this connection
     * will result in ResourceException being thrown.
     *
     * @throws ResourceException if this connection is already closed, or
     *  could not be closed due to a system-level failure.
     */
    public void close()
        throws ResourceException;
}

