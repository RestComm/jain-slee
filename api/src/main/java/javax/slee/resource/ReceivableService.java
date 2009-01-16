package javax.slee.resource;

import javax.slee.EventTypeID;
import javax.slee.ServiceID;

/**
 * The <code>ReceivableService</code> interface provides information about the event
 * types that may be received by a service installed in the SLEE.
 * @since SLEE 1.1
 */
public interface ReceivableService {
    /**
     * The <code>ReceiveableEvent</code> interface identifies information about an
     * event type that may be received by SBBs in a service.
     * @since SLEE 1.1
     */
    public interface ReceivableEvent {
        /**
         * Get the component identifier of the event type.
         * <p>
         * This method is a non-transactional method.
         * @return the component identifier of the event type.
         */
        public EventTypeID getEventType();

        /**
         * Get the resource option specified by the SBB receiving the event type.
         * <p>
         * This method is a non-transactional method.
         * @return the resource option.  May be <code>null</code> if the SBB did not
         *        specify a resource option.
         */
        public String getResourceOption();

        /**
         * Get the initial-event flag for the event type.  An event type can only be
         * an initial event if the event type is received by the service's root SBB and
         * the root SBB has declared the event type as an initial event.
         * <p>
         * This method is a non-transactional method.
         * @return <code>true</code> if the event type is an initial event for the service,
         *        <code>false</code> otherwise.
         */
        public boolean isInitialEvent();
    }


    /**
     * Get the component identifier of the service represented by this
     * <code>ReceivableService</code> object.
     * <p>
     * This method is a non-transactional method.
     * @return the component identifier of the service.
     */
    public ServiceID getService();

    /**
     * Get information about the event types that can be received by SBBs in the service.
     * <p>
     * If different SBBs in the service have specified to receive the same event type
     * but with different resource options, then separate <code>ReceivableEvent</code>
     * objects are included in the returned array for each unique resource option.
     * <p>
     * This method is a non-transactional method.
     * @return an array of <code>ReceivableEvent</code> objects that contain information
     *        about the event types that can be received by SBBs in the service.
     */
    public ReceivableEvent[] getReceivableEvents();

    /**
     * Compare this <code>ReceivableService</code> object for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a <code>ReceivableService</code>
     *        object and the service component identifier of this object equals the service
     *        component identifier contained by <code>obj</code>.
     * @see Object#equals(Object)
     */
    public boolean equals(Object obj);
}
