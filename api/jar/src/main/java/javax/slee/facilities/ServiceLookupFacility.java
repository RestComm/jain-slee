package javax.slee.facilities;

import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.resource.ReceivableService;

/**
 * The Service Lookup Facility is used by resource adaptors to obtain information about the
 * event types a service may received.  A resource adaptor may obtain an instance of the Event
 * Lookup Facility via the {@link javax.slee.resource.ResourceAdaptorContext#getServiceLookupFacility()}
 * method.
 * @since SLEE 1.1
 */
public interface ServiceLookupFacility {
    /**
     * Get information about the event types that a service may receive.
     * <p>
     * The SLEE need only provide a resource adaptor with information about the event
     * types that the resource adaptor may fire.  Generally this is limited to the
     * resource adaptor types implemented by the resource adaptor.  However a resource
     * adaptor may be able to fire events of any type if its deployment descriptor
     * has disabled this limitation, and in such cases the Service Lookup Facility should
     * provide the resource adaptor with information about all the event types that may
     * be received by the service.
     * <p>
     * This method is a non-transactional method.
     * @param service the service component identifier for the service.
     * @return a <code>ReceivableService</code> object that contains information about
     *        the event types that may be received by the service.
     * @throws NullPointerException if <code>service</code> is <code>null</code>.
     * @throws UnrecognizedServiceException if <code>service</code> does not identify a
     *        service installed in the SLEE.
     * @throws FacilityException if the <code>ReceivableService</code> object could not be
     *        obtained due to a system-level failure.
     */
    public ReceivableService getReceivableService(ServiceID service)
        throws NullPointerException, UnrecognizedServiceException, FacilityException;
}
